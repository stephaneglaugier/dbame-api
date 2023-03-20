package com.saugier.dbame.core.service.impl;

import com.saugier.dbame.core.model.base.*;
import com.saugier.dbame.core.service.ICryptoService;
import com.saugier.dbame.core.service.IElectionService;
import org.slf4j.Logger;
import org.apache.commons.codec.DecoderException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Hash;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.SecureRandom;
import java.util.*;

@Service
public class CryptoServiceImpl implements ICryptoService {

    @Autowired
    Logger log;

    @Autowired
    IElectionService electionService;

    public static int DEFAULT_RADIX = 16;

    private final BigInteger privateKey;
    private final BigInteger registrarPublicKey;
    private final BigInteger moderatorPublicKey;

    private final BigInteger prime;
    private final BigInteger generator;
    private final byte[] iv;

    public CryptoServiceImpl(
            @Value("${user.key.private}") String pk,
            @Value("${registrar.key.public}") String rpk,
            @Value("${moderator.key.public}") String mpk,
            @Autowired IElectionService electionService
    ) throws DecoderException {
        privateKey = new BigInteger(pk, DEFAULT_RADIX);
        registrarPublicKey = new BigInteger(rpk, DEFAULT_RADIX);
        moderatorPublicKey = new BigInteger(mpk, DEFAULT_RADIX);
        prime = electionService.getP();
        generator = electionService.getG();
        iv = electionService.getIv();
    }

    
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

    
    public BigInteger randomlySelect(BigInteger upperBound){
        Random rand = new Random();
        BigInteger out = new BigInteger(upperBound.bitLength(), rand);
        while( out.compareTo(upperBound) > 0 ) {
            out = new BigInteger(upperBound.bitLength(), rand);
        }
        return out;
    }


    
    public Ballot sign(Ballot ballot) throws Exception {
        ballot.setSignature(sign(ballot.asMessage()));
        return ballot;
    }

    
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

        BigInteger _pMinus1 = prime.subtract(BigInteger.ONE);
        BigInteger _u = randomCoprime(_pMinus1);
        BigInteger _w = generator.modPow(_u, prime);
        BigInteger _hashedMessage = new BigInteger(Hash.sha256(bytes));
        BigInteger _s =
                (_hashedMessage.subtract(privateKey.multiply(_w)))
                        .multiply(_u.modInverse(_pMinus1))
                        .mod(_pMinus1);

        Signature out = new Signature(_w, _s);

        return out;
    }

    
    public boolean validate(Roll roll) {
        BigInteger _w = roll.getSignature().getW();
        BigInteger _s = roll.getSignature().getS();

        if (_w.compareTo(prime) > -1){
            log.warn("INVALID ROLL: w greater than p");
            return false;
        }
        if (_s.compareTo(prime.subtract(BigInteger.ONE)) > -1){
            log.warn("INVALID ROLL: s greater than p-1");
            return false;
        }

        BigInteger _hashedMessage = new BigInteger(Hash.sha256(roll.getPublicKey().toByteArray()));

        BigInteger _a = generator.modPow(_hashedMessage, prime);
        BigInteger _b = registrarPublicKey.modPow(_w, prime).multiply(_w.modPow(_s, prime)).mod(prime);

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

    
    public EncryptedBallot encryptBallot(Ballot ballot, BigInteger maskedY, long permutation) throws Exception{
        BigInteger _q = randomlySelect(prime);
        BigInteger _k = maskedY.modPow(_q, prime);

        EncryptedBallot out = new EncryptedBallot();
        out.setCypherText(AESEncrypt(ballot.asMessage(), _k));

        out.setEphemeralKey(generator.modPow(_q, prime));

        return out;
    }

    private String AESEncrypt(String message, BigInteger password) throws Exception {

        log.warn("message = " + message);
        log.warn("full password = " + password.toString(DEFAULT_RADIX));

        String passwordString = keyToPassword(password, 32);

        log.warn("password = " + passwordString);

        byte[] fLBAKey = passwordString.getBytes(StandardCharsets.UTF_8);
        log.warn("fLBAKey = " + Arrays.toString(fLBAKey));

        Key secretKey = new SecretKeySpec(fLBAKey, "AES");
//        log.warn("secretKey = " + Arrays.toString(secretKey.getEncoded()));

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv));

        byte[] encrypted = cipher.doFinal(message.getBytes());
        String encoded = Base64.getEncoder().encodeToString(encrypted);
//        log.warn("encrypted = " + Arrays.toString(encrypted));
        log.warn("encoded = " + encoded);

        Key secretKey2 = new SecretKeySpec(fLBAKey, "AES");
//        log.warn("secretKey2 = " + Arrays.toString(secretKey2.getEncoded()));

        Cipher cipher2 = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher2.init(Cipher.DECRYPT_MODE, secretKey2, new IvParameterSpec(iv));


        byte[] decoded = Base64.getDecoder().decode(encoded);
        byte[] decrypted = cipher2.doFinal(decoded);
//        log.warn("decoded = " + Arrays.toString(decoded));
//        log.warn("decrypted = " + new String(decrypted));

        return encoded;
    }

    /**
     * Converts a BigInteger key into a password string of a specified length.
     *
     * @param key     The BigInteger key to be converted into a password string.
     * @param length  The desired length of the resulting password string.
     * @return        A password string of the specified length derived from the BigInteger key.
     * @throws Exception If the resulting password string's length does not match the specified length.
     *
     * This method converts a given BigInteger key into a password string with a specified length.
     * If the length of the key string is greater than the specified length, it adds leading zeros
     * until the desired length is reached. If the length of the key string is smaller than the specified
     * length, it truncates the key string from the left side until the desired length is reached.
     * If the length of the resulting password string is not equal to the specified length,
     * an exception is thrown.
     */
    private String keyToPassword(BigInteger key, int length) throws Exception{
        String _keyString = key.toString(DEFAULT_RADIX);
        String out;

        if (_keyString.length() < length) {
            int nZeros = length - _keyString.length();
            String zeros = "";
            for (int i=0; i < nZeros; i++){
                zeros = zeros + "0";
            }
            out = zeros + _keyString;
        } else if (_keyString.length() > length) {
            out = _keyString.substring(_keyString.length()-length);
        } else {
            out = _keyString;
        }

        if (out.length() != length) {
            throw new Exception(String.format("Error when converting key to password. %s != %s", out.length(), length));
        }
        return out;
    }

    
    public Mask mask(BigInteger bigInteger) {

        BigInteger blindFactor = randomlySelect(prime);
        BigInteger mask = bigInteger.modPow(blindFactor, prime);

        Mask out = new Mask(mask, blindFactor);

        return out;
    }

    
    public EncryptedBlindFactor encrypt(BigInteger blindFactor, BigInteger voterPublicKey) {

        BigInteger _rm = randomlySelect(prime);

        BigInteger c1 = generator.modPow(_rm, prime);
        BigInteger c2 = blindFactor.multiply(voterPublicKey.modPow(_rm, prime));
        EncryptedBlindFactor out = new EncryptedBlindFactor(c1, c2);

        log.warn(
            String.format(
                "encrypting blind factor %s with key %s",
                blindFactor.toString(DEFAULT_RADIX),
                voterPublicKey.toString(DEFAULT_RADIX)
            )
        );

        return out;
    }

    
    public Map.Entry<BigInteger, BigInteger> generatePrimeAndGenerator(int bitLength) {
        SecureRandom random = new SecureRandom();
        BigInteger p, g;

        // Generate a prime number of specified bit length
        do {
            p = BigInteger.probablePrime(bitLength, random);
        } while (!p.isProbablePrime(100)); // Ensure prime with high probability

        // Find a generator for the group modulo p
        g = findGenerator(p, random);

        // Return prime number and generator as a Map.Entry
        Map.Entry<BigInteger, BigInteger> result = new HashMap.SimpleEntry<>(p, g);
        return result;
    }

    // Helper method to find a generator for a prime number p
    public static BigInteger findGenerator(BigInteger p, SecureRandom random) {
        BigInteger phi = p.subtract(BigInteger.ONE); // phi(p) = p - 1
        BigInteger TWO = new BigInteger("2");
        BigInteger g;

        do {
            // Generate a random integer g between 2 and p-1
            g = new BigInteger(phi.bitLength(), random).mod(phi).add(TWO);
        } while (!(g.modPow(phi.divide(TWO), p).equals(BigInteger.ONE))
                || !(g.modPow(phi.divide(BigInteger.valueOf(3)), p).equals(BigInteger.ONE)));

        return g;
    }

    
    public BigInteger generatePrivateKey(BigInteger p, int bitLength) {
        if (bitLength >= p.bitLength()) {
            throw new IllegalArgumentException("bitLength must be less than p's bit length");
        }

        SecureRandom random = new SecureRandom();
        BigInteger privateKey;

        do {
            // Generate a random integer with the desired bit length
            privateKey = new BigInteger(bitLength, random);
        } while (privateKey.compareTo(BigInteger.ZERO) <= 0 || privateKey.compareTo(p) >= 0);

        return privateKey;
    }
}

