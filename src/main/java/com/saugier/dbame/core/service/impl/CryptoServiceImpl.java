package com.saugier.dbame.core.service.impl;

import com.saugier.dbame.core.model.BallotRequest;
import com.saugier.dbame.core.model.BallotResponse;
import com.saugier.dbame.core.service.ICryptoService;
import com.saugier.dbame.registrar.model.entity.Ballot;
import com.saugier.dbame.registrar.model.entity.Roll;
import com.saugier.dbame.registrar.model.entity.SignedBallot;
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
        BigInteger _u = randomCoprime(prime.subtract(TWO));
        BigInteger _w = generator.modPow(_u, prime);
        BigInteger _hashedMessage = new BigInteger(Hash.sha256(DatatypeConverter.parseHexBinary(message)));
        BigInteger _s = _hashedMessage.subtract(privateKey.multiply(_w)).divide(_u).mod(prime).subtract(BigInteger.ONE);

        HashMap<String, String> out = new HashMap();
        out.put("s", _s.toString(16));
        out.put("w", _w.toString(16));
        return out;
    }

    private BigInteger randomCoprime(BigInteger in){
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
    public Roll encrypt(Roll roll) {

        BigInteger _blindFactor = randomlySelect(prime);
        BigInteger _result = new BigInteger(roll.getY()).modPow(_blindFactor, prime);
        roll.setY(_result.toString(16));
        return roll;
    }
}
