package com.saugier.dbame.core.service.impl;

import com.saugier.dbame.core.model.BallotRequest;
import com.saugier.dbame.core.model.BallotResponse;
import com.saugier.dbame.core.model.entity.Roll;
import com.saugier.dbame.core.service.ICryptoService;
import com.saugier.dbame.moderator.model.entity.EncryptedBallot;
import com.saugier.dbame.registrar.model.entity.Ballot;
import com.saugier.dbame.registrar.model.entity.SignedBallot;
import com.sun.org.slf4j.internal.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Hash;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.xml.bind.DatatypeConverter;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
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
    public Roll sign(Roll roll) {
        HashMap<String, String> sw = sign(roll.getY());
        roll.setW(sw.get("w"));
        roll.setS(sw.get("s"));
        return roll;
    }

    @Override
    public SignedBallot sign(Ballot ballot) {
        HashMap<String, String> sw = sign(ballot.to256Hex());
        SignedBallot out = new SignedBallot();
        out.setId(ballot.getId());
        out.setTimestamp(ballot.getTimestamp());
        out.setRandint(ballot.getRandint());
        out.setW(sw.get("w"));
        out.setS(sw.get("s"));
        return out;
    }

    private HashMap<String, String> sign(String message) {
        BigInteger _pMinus1 = prime.subtract(BigInteger.ONE);
        BigInteger _u = randomCoprime(_pMinus1);
        log.warn(_u.toString(10));
        BigInteger _w = generator.modPow(_u, prime);
        BigInteger _hashedMessage = new BigInteger(snip(Hash.sha256(DatatypeConverter.parseHexBinary(message)), 0, 16));
        BigInteger _s = (_hashedMessage.subtract(privateKey.multiply(_w))).multiply(_u.modInverse(_pMinus1)).mod(_pMinus1);

        HashMap<String, String> out = new HashMap();
        out.put("s", _s.toString(DEFAULT_RADIX));
        out.put("w", _w.toString(DEFAULT_RADIX));
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
    public BallotResponse encryptBallot(SignedBallot signedBallot, BallotRequest ballotRequest) throws Exception{
        BigInteger _q = randomlySelect(prime);
        BigInteger _y = new BigInteger(ballotRequest.getMask(), DEFAULT_RADIX);
        BigInteger _k = _y.modPow(_q, prime);

        BallotResponse out = new BallotResponse();
        out.setCiphertext(AESEncrypt(signedBallot.to256Hex(), _k));

        BigInteger _Q = generator.modPow(_q, prime);
        out.setEphemeralKey(_Q.toString(DEFAULT_RADIX));

        return out;
    }

    private String AESEncrypt(String message, BigInteger key) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

        IvParameterSpec iv = generateIv(DEFAULT_RADIX);
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, generateKey(128));//, iv);
        byte[] cipherText = cipher.doFinal(message.getBytes());
        return DatatypeConverter.printHexBinary(cipherText);
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

    // moderator functions
    @Override
    public EncryptedBallot encrypt(Roll roll) {

        EncryptedBallot out = new EncryptedBallot();

        BigInteger _blindFactor = randomlySelect(prime);
        BigInteger _result = new BigInteger(roll.getY(), DEFAULT_RADIX).modPow(_blindFactor, prime);
        roll.setY(_result.toString(DEFAULT_RADIX));

        out.setRoll(roll);
        out.setBlindFactor(_blindFactor.toString(DEFAULT_RADIX));
        return out;
    }


    @Override
    public boolean validate(Roll roll) {
        BigInteger _y = new BigInteger(roll.getY(), DEFAULT_RADIX);
        BigInteger _w = new BigInteger(roll.getW(), DEFAULT_RADIX);
        BigInteger _s = new BigInteger(roll.getS(), DEFAULT_RADIX);

        if (_w.compareTo(prime) > -1){
            log.warn("INVALID ROLL: w greater than p");
            return false;
        }
        if (_s.compareTo(prime.subtract(BigInteger.ONE)) > -1){
            log.warn("INVALID ROLL: s greater than p-1");
            return false;
        }

        BigInteger _a = generator.modPow(new BigInteger(Arrays.copyOfRange(Hash.sha256(_y.toByteArray()), 0, 16)), prime);
        BigInteger _b = registrarPublicKey.modPow(_w, prime).multiply(_w.modPow(_s, prime)).mod(prime);

        if (_a.compareTo(_b) != 0){
            log.warn(String.format("INVALID ROLL: %s != %s", _a.toString(DEFAULT_RADIX), _b.toString(DEFAULT_RADIX)));
            return false;
        }
        return true;
    }
}
