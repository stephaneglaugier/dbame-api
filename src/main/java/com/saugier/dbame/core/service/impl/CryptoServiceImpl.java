package com.saugier.dbame.core.service.impl;

import com.saugier.dbame.core.model.base.*;
import com.saugier.dbame.core.service.ICryptoService;
import com.sun.org.slf4j.internal.Logger;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Hash;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.security.Key;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;

@Service
public class CryptoServiceImpl implements ICryptoService {

    @Autowired
    Logger log;

    public static int DEFAULT_RADIX = 16;
    java.math.BigInteger TWO = new java.math.BigInteger("2");

    private final java.math.BigInteger privateKey;
    private final java.math.BigInteger prime;
    private final java.math.BigInteger generator;
    private final java.math.BigInteger registrarPublicKey;
    private final java.math.BigInteger moderatorPublicKey;

    public CryptoServiceImpl(
            @Value("${user.key.private}") String pk,
            @Value("${global.key.p}") String p,
            @Value("${global.key.g}") String g,
            @Value("${registrar.key.public}") String rpk,
            @Value("${moderator.key.public}") String mpk
    ){
        privateKey = new java.math.BigInteger(pk, DEFAULT_RADIX);
        prime = new java.math.BigInteger(p, DEFAULT_RADIX);
        generator = new java.math.BigInteger(g, DEFAULT_RADIX);
        registrarPublicKey = new java.math.BigInteger(rpk, DEFAULT_RADIX);
        moderatorPublicKey = new java.math.BigInteger(mpk, DEFAULT_RADIX);
    }

    @Override
    public java.math.BigInteger randomCoprime(java.math.BigInteger in){
        java.math.BigInteger out = randomlySelect(in);
        while (!isRelativelyPrime(in, out)){
            out = randomlySelect(in);
        }
        return out;
    }

    private boolean isRelativelyPrime(java.math.BigInteger a, java.math.BigInteger b){
        return a.gcd(b).equals(java.math.BigInteger.ONE);
    }

    @Override
    public java.math.BigInteger randomlySelect(java.math.BigInteger upperBound){
        Random rand = new Random();
        java.math.BigInteger out = new java.math.BigInteger(upperBound.bitLength(), rand);
        while( out.compareTo(upperBound) > 0 ) {
            out = new java.math.BigInteger(upperBound.bitLength(), rand);
        }
        return out;
    }


    @Override
    public Ballot sign(Ballot ballot) throws Exception {
        ballot.setSignature(sign(ballot.asMessage()));
        return ballot;
    }

    @Override
    public Roll sign(Roll roll) {
        roll.setSignature(sign(roll.getPublicKey()));
        return roll;
    }

    public Signature sign(String s) {
        return sign(s.getBytes());
    }

    public Signature sign(BigInteger bigInteger) {
        return sign(bigInteger.toByteArray());
    }

    public Signature sign(byte[] bytes) {

        java.math.BigInteger _pMinus1 = prime.subtract(java.math.BigInteger.ONE);
        java.math.BigInteger _u = randomCoprime(_pMinus1);
        java.math.BigInteger _w = generator.modPow(_u, prime);
        java.math.BigInteger _hashedMessage = new java.math.BigInteger(Hash.sha256(bytes));
        java.math.BigInteger _s =
                (_hashedMessage.subtract(privateKey.multiply(_w)))
                        .multiply(_u.modInverse(_pMinus1))
                        .mod(_pMinus1);

        Signature out = new Signature(_w, _s);

        return out;
    }

    @Override
    public boolean validate(Roll roll) {
        java.math.BigInteger _w = roll.getSignature().getW();
        java.math.BigInteger _s = roll.getSignature().getS();

        if (_w.compareTo(prime) > -1){
            log.warn("INVALID ROLL: w greater than p");
            return false;
        }
        if (_s.compareTo(prime.subtract(java.math.BigInteger.ONE)) > -1){
            log.warn("INVALID ROLL: s greater than p-1");
            return false;
        }

        java.math.BigInteger _hashedMessage = new java.math.BigInteger(Hash.sha256(roll.getPublicKey().toByteArray()));

        java.math.BigInteger _a = generator.modPow(_hashedMessage, prime);
        java.math.BigInteger _b = registrarPublicKey.modPow(_w, prime).multiply(_w.modPow(_s, prime)).mod(prime);

        if (_a.mod(prime).compareTo(_b.mod(prime)) != 0){
            log.warn(String.format("INVALID ROLL: %s != %s", _a.toString(DEFAULT_RADIX), _b.toString(DEFAULT_RADIX)));
            return false;
        }
        return true;
    }

    private byte[] snip( byte[] in, int from, int to){

        byte[] out = new byte[to-from];
        for (int i=0; i<out.length; i++){
            out[i] = in[i+from];
        }
        return out;
    }

    @Override
    public EncryptedBallot encryptBallot(Ballot ballot, BigInteger maskedY, long permutation) throws Exception{
        BigInteger _q = randomlySelect(prime);
        BigInteger _k = maskedY.modPow(_q, prime);

        EncryptedBallot out = new EncryptedBallot();
        out.setCypherText(AESEncrypt(ballot.asMessage(), _k));

        out.setEphemeralKey(generator.modPow(_q, prime));

        return out;
    }

    private String AESEncrypt(String message, BigInteger password) throws Exception {

//        byte[] salt = Hex.decodeHex("123456");
//        int iterationCount = 100;
//        int keySize = 128;
//        byte[] iv = Hex.decodeHex("F27D5C9927726BCEFE7510B1BDD3D137");
//
//        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
//
//        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
//        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterationCount, keySize);
//        SecretKey key = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
//
//        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
//        byte[] encrypted = cipher.doFinal(message.getBytes());
//
//        String encoded = Base64.getEncoder().encodeToString(encrypted);
//
//        return encoded;


        log.warn("message = " + message);
        log.warn("password = " + password.toString(DEFAULT_RADIX));

        byte[] fLBAKey = java.util.Arrays.copyOf(password.toByteArray() , 32);
        log.warn("fLBAKey = " + Arrays.toString(fLBAKey));

        Key secretKey = new SecretKeySpec(fLBAKey, "AES");
        log.warn("secretKey = " + Arrays.toString(secretKey.getEncoded()));

        byte[] iv = Hex.decodeHex("F27D5C9927726BCEFE7510B1BDD3D137");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv));

        byte[] encrypted = cipher.doFinal(message.getBytes());
        String encoded = Base64.getEncoder().encodeToString(encrypted);
        log.warn("encrypted = " + Arrays.toString(encrypted));
        log.warn("encoded = " + encoded);

        Key secretKey2 = new SecretKeySpec(fLBAKey, "AES");
        log.warn("secretKey2 = " + Arrays.toString(secretKey2.getEncoded()));

        Cipher cipher2 = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher2.init(Cipher.DECRYPT_MODE, secretKey2, new IvParameterSpec(iv));


        byte[] decoded = Base64.getDecoder().decode(encoded);
        byte[] decrypted = cipher2.doFinal(decoded);
        log.warn("decoded = " + Arrays.toString(decoded));
        log.warn("decrypted = " + new String(decrypted));

        return encoded;
    }

    @Override
    public Mask mask(BigInteger bigInteger) {

        BigInteger blindFactor = randomlySelect(prime);
        BigInteger mask = bigInteger.modPow(blindFactor, prime);

        Mask out = new Mask(mask, blindFactor);

        return out;
    }

    @Override
    public EncryptedBlindFactor encrypt(BigInteger blindFactor, BigInteger voterPublicKey) {

        java.math.BigInteger _rm = randomlySelect(prime);

        BigInteger c1 = generator.modPow(_rm, prime);
        BigInteger c2 = blindFactor.multiply(voterPublicKey.modPow(_rm, prime));
        EncryptedBlindFactor out = new EncryptedBlindFactor(c1, c2);

        return out;
    }
}
