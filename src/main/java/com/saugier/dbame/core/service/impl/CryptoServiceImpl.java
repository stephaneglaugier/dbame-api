package com.saugier.dbame.core.service.impl;

import com.saugier.dbame.core.service.ICryptoService;
import com.saugier.dbame.registrar.model.Ballot;
import com.saugier.dbame.registrar.model.BallotResponse;
import com.saugier.dbame.registrar.model.entity.BallotRequest;
import com.saugier.dbame.registrar.model.entity.Roll;
import com.saugier.dbame.registrar.model.entity.SignedBallot;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Hash;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Random;

@Service
public class CryptoServiceImpl implements ICryptoService {


    BigInteger TWO = new BigInteger("2");

    private final BigInteger privateKey;
    private final BigInteger prime;
    private final BigInteger generator;

    public CryptoServiceImpl(
            @Value("${user.key.private}") String pk,
            @Value("${global.key.p}") String p,
            @Value("${global.key.g}") String g){
        privateKey = new BigInteger(DatatypeConverter.parseHexBinary(pk));
        prime = new BigInteger(DatatypeConverter.parseHexBinary(p));
        generator = new BigInteger(DatatypeConverter.parseHexBinary(g));
    }

    @Override
    public boolean isRelativelyPrime(BigInteger a, BigInteger b){
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
    public BigInteger randomCoprime(BigInteger in){
        BigInteger out = randomlySelect(in);
        while (!isRelativelyPrime(in, out)){
            out = randomlySelect(in);
        }
        return out;
    }

    @Override
    public BigInteger computePublicKey() {
        return generator.modPow(privateKey, prime);
    }

    @Override
    public Roll EGSignRoll(Roll roll) {
        HashMap<String, String> sw = EGSign(roll.getY());
        roll.setW(sw.get("w"));
        roll.setS(sw.get("s"));
        return roll;
    }


    // moderator function
    @Override
    public Roll encryptRoll(Roll roll) {

        BigInteger _blindFactor = randomlySelect(prime);
        BigInteger _result = new BigInteger(roll.getY()).modPow(_blindFactor, prime);
        roll.setY(_result.toString(16));
        return roll;
    }

    @Override
    public SecretKey generateKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(64);
        return keyGenerator.generateKey();
    }

    @Override
    public SealedObject encryptObject(String algorithm, Serializable object,
                                             SecretKey key, IvParameterSpec iv) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidAlgorithmParameterException,
            InvalidKeyException, IOException, IllegalBlockSizeException {

        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        return new SealedObject(object, cipher);
    }

    @Override
    public Serializable decryptObject(String algorithm, SealedObject sealedObject,
                                             SecretKey key, IvParameterSpec iv) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException,
            ClassNotFoundException, BadPaddingException, IllegalBlockSizeException,
            IOException {

        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        return (Serializable) sealedObject.getObject(cipher);
    }

    @Override
    public SignedBallot sign(Ballot b) {
        HashMap<String, String> sw = EGSign(b.to256Hex());
        SignedBallot out = new SignedBallot();
        out.setId(b.getId());
        out.setTimestamp(b.getTimestamp());
        out.setRandint(b.getRandint());
        out.setW(sw.get("w"));
        out.setS(sw.get("s"));
        return out;
    }

    @Override
    public HashMap<String, String> EGSign(String message) {
        BigInteger _u = randomCoprime(prime.subtract(TWO));
        BigInteger _w = generator.modPow(_u, prime);
        BigInteger _hashedMessage = new BigInteger(Hash.sha256(DatatypeConverter.parseHexBinary(message)));
        BigInteger _s = _hashedMessage.subtract(privateKey.multiply(_w)).divide(_u).mod(prime).subtract(BigInteger.ONE);

        HashMap<String, String> out = new HashMap();
        out.put("s", _s.toString(16));
        out.put("w", _w.toString(16));
        return out;
    }

    @Override
    public BallotResponse encryptBallot(SignedBallot signedBallot, BallotRequest ballotRequest) throws Exception{
        BigInteger _q = randomlySelect(prime);
        BigInteger _y = new BigInteger(ballotRequest.getMask(), 16);
        BigInteger _k = _y.modPow(_q, prime);

        BallotResponse out = new BallotResponse();
        out.setCiphertext(AESEncrypt(signedBallot.to256Hex(), _k));

        BigInteger _Q = generator.modPow(_q, prime);
        out.setEphemeralKey(_Q.toString(16));

        return out;
    }

    private String AESEncrypt(String message, BigInteger key) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

        IvParameterSpec iv = generateIv(16);
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, generateKey(128));//, iv);
        byte[] cipherText = cipher.doFinal(message.getBytes());
        return DatatypeConverter.printHexBinary(cipherText);
    }

    public static SecretKey generateKey(int n) throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(n);
        SecretKey key = keyGenerator.generateKey();
        return key;
    }

    public static IvParameterSpec generateIv(int n) {
        byte[] iv = new byte[n];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }
}
