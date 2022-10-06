package com.saugier.dbame.core.service.impl;

import com.saugier.dbame.core.model.base.*;
import com.saugier.dbame.core.service.ICryptoService;
import com.sun.org.slf4j.internal.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Hash;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Random;

@Service
public class CryptoServiceImpl implements ICryptoService {

    @Autowired
    Logger log;

    public static int DEFAULT_RADIX = 16;
    BigInteger TWO = new BigInteger("2");

    private final BigInteger privateKey;
    private final BigInteger prime;
    private final BigInteger generator;
    private final BigInteger registrarPublicKey;
    private final BigInteger moderatorPublicKey;

    public CryptoServiceImpl(
            @Value("${user.key.private}") String pk,
            @Value("${global.key.p}") String p,
            @Value("${global.key.g}") String g,
            @Value("${registrar.key.public}") String rpk,
            @Value("${moderator.key.public}") String mpk
    ){
        privateKey = new BigInteger(pk, DEFAULT_RADIX);
        prime = new BigInteger(p, DEFAULT_RADIX);
        generator = new BigInteger(g, DEFAULT_RADIX);
        registrarPublicKey = new BigInteger(rpk, DEFAULT_RADIX);
        moderatorPublicKey = new BigInteger(mpk, DEFAULT_RADIX);
    }

    @Override
    public BigInteger randomCoprime(BigInteger in){
        BigInteger out = randomlySelect(in);
        while (!isRelativelyPrime(in, out)){
            out = randomlySelect(in);
        }
        return out;
    }

    private boolean isRelativelyPrime(BigInteger a, BigInteger b){
        return a.gcd(b).equals(BigInteger.ONE);
    }

    @Override
    public BigInteger randomlySelect(BigInteger upperBound){
        Random rand = new Random();
        BigInteger out = new BigInteger(upperBound.bitLength(), rand);
        while( out.compareTo(upperBound) > 0 ) {
            out = new BigInteger(upperBound.bitLength(), rand);
        }
        return out;
    }


    @Override
    public Ballot sign(Ballot ballot) throws Exception {
        ballot.setSignature(sign(ballot.toDatum()));
        return ballot;
    }

    @Override
    public Roll sign(Roll roll) {
        roll.setSignature(sign(roll.getPublicKey()));
        return roll;
    }

    @Deprecated
    private HashMap<String, String> sign(String message) {

        Datum datum = new Datum(message);
        Signature signature = sign(datum);

        HashMap<String, String> out = new HashMap();
        out.put("w", signature.getW().toString());
        out.put("s", signature.getS().toString());

        return out;
    }

    private byte[] snip( byte[] in, int from, int to){

        byte[] out = new byte[to-from];
        for (int i=0; i<out.length; i++){
            out[i] = in[i+from];
        }
        return out;
    }

    @Override
    public EncryptedBallot encryptBallot(Ballot ballot, Datum maskedY, long permutation) throws Exception{
        BigInteger _q = randomlySelect(prime);
        BigInteger _y = maskedY.toBigInt();
        BigInteger _k = _y.modPow(_q, prime);

        EncryptedBallot out = new EncryptedBallot();
        out.setCypherText(AESEncrypt(ballot.toDatum(), _k));

        out.setEphemeralKey(new Datum(generator.modPow(_q, prime)));

        return out;
    }

    private Datum AESEncrypt(Datum message, BigInteger key) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

        IvParameterSpec iv = generateIv(DEFAULT_RADIX);
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, generateKey(128));//, iv);
        byte[] cipherText = cipher.doFinal(message.toBytes());

        Datum out = new Datum(cipherText);

        return out;
    }

    private IvParameterSpec generateIv(int n) {
        byte[] iv = new byte[n];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    private SecretKey generateKey(int n) throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(n);
        SecretKey key = keyGenerator.generateKey();
        return key;
    }

//    // moderator functions
//    @Override
//    public ModeratorRelayME encrypt(RollRE rollRE) {
//
//        ModeratorRelayME out = new ModeratorRelayME();
//
//        BigInteger _blindFactor = randomlySelect(prime);
//        BigInteger _result = new BigInteger(rollRE.getY(), DEFAULT_RADIX).modPow(_blindFactor, prime);
//        rollRE.setY(_result.toString(DEFAULT_RADIX));
//
////        out.setRoll(rollRE);
//        out.setBlindFactor(_blindFactor.toString(DEFAULT_RADIX));
//        return out;
//    }


    @Override
    public boolean validate(Roll roll) {
        BigInteger _w = roll.getSignature().getW().toBigInt();
        BigInteger _s = roll.getSignature().getS().toBigInt();

        if (_w.compareTo(prime) > -1){
            log.warn("INVALID ROLL: w greater than p");
            return false;
        }
        if (_s.compareTo(prime.subtract(BigInteger.ONE)) > -1){
            log.warn("INVALID ROLL: s greater than p-1");
            return false;
        }

        BigInteger _hashedMessage = new BigInteger(snip(Hash.sha256(roll.getPublicKey().toBytes() ), 0, 2));

        BigInteger _a = generator.modPow(_hashedMessage, prime);
        BigInteger _b = registrarPublicKey.modPow(_w, prime).multiply(_w.modPow(_s, prime)).mod(prime);

        if (_a.compareTo(_b) != 0){
            log.warn(String.format("INVALID ROLL: %s != %s", _a.toString(DEFAULT_RADIX), _b.toString(DEFAULT_RADIX)));
            return false;
        }
        return true;
    }

    @Override
    public Signature sign(Datum datum) {
        Signature out = new Signature();

        BigInteger _pMinus1 = prime.subtract(BigInteger.ONE);
        BigInteger _u = randomCoprime(_pMinus1);
        BigInteger _w = generator.modPow(_u, prime);
        BigInteger _hashedMessage = new BigInteger(snip(Hash.sha256(datum.toBytes()), 0, 2));
        BigInteger _s = (_hashedMessage.subtract(privateKey.multiply(_w))).multiply(_u.modInverse(_pMinus1)).mod(_pMinus1);

        log.warn(_hashedMessage.toString(DEFAULT_RADIX));

        out.setW(new Datum((_w)));
        out.setS(new Datum((_s)));

        return out;
    }

    @Override
    public Mask mask(Datum datum) {

        Mask out = new Mask();

        BigInteger _blindFactor = randomlySelect(prime);

        out.setMask(new Datum(datum.toBigInt().modPow(_blindFactor, prime)));
        out.setBlindFactor(new Datum(_blindFactor));

        return out;
    }

    @Override
    public EncryptedBlindFactor encrypt(Datum blindFactor, Datum voterPublicKey) {

        EncryptedBlindFactor out = new EncryptedBlindFactor();
        BigInteger _rm = randomlySelect(prime);

        out.setC1(new Datum(generator.modPow(_rm, prime)));
        out.setC2(new Datum(blindFactor.toBigInt().multiply(voterPublicKey.toBigInt().modPow(_rm, prime))));

        return out;
    }


}
