package com.saugier.dbame.core.service.impl;

import com.saugier.dbame.core.service.ICryptoService;
import com.saugier.dbame.registrar.model.entity.Roll;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Hash;

import javax.xml.bind.DatatypeConverter;
import java.math.BigInteger;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class CryptoServiceImpl implements ICryptoService {

    BigInteger TWO = new BigInteger("2");

    private final BigInteger privateKey;
    private final long prime;
    private final long generator;

    public CryptoServiceImpl(
            @Value("${user.key.private}") String pk,
            @Value("${global.key.p}") String p,
            @Value("${global.key.g}") String g){
        privateKey = new BigInteger(DatatypeConverter.parseHexBinary(pk));
        prime = Long.parseLong(p, 16);
        generator = Long.parseLong(g, 16);
    }

    @Override
    public boolean isRelativelyPrime(BigInteger a, BigInteger b){
        return a.gcd(b).equals(BigInteger.ONE);
    }

    @Override
    public long randomlySelect(long upperBound) {
        return ThreadLocalRandom.current().nextLong(upperBound);
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
        return BigInteger.valueOf(generator).modPow(privateKey, BigInteger.valueOf(prime));
    }

    @Override
    public Roll EGSignRoll(Roll roll) {
        BigInteger _u = randomCoprime(BigInteger.valueOf(prime).subtract(TWO));
        BigInteger _w = BigInteger.valueOf(generator).modPow(_u, BigInteger.valueOf(prime));
        BigInteger _hashedMessage = new BigInteger(Hash.sha256(DatatypeConverter.parseHexBinary(roll.getKey().getBytes())));
        BigInteger _s = _hashedMessage.subtract(privateKey.multiply(_w)).divide(_u).mod(BigInteger.valueOf(prime).subtract(BigInteger.ONE));

        roll.setW(_w.toString(16));
        roll.setS(_s.toString(16));
        return roll;
    }


    @Override
    public Roll encryptRoll(Roll roll) {

        long _blindFactor = randomlySelect(prime);
        BigInteger _result = roll.getKey().asBI().pow((int) _blindFactor);
        roll.getKey().setBytes(_result.toString(16));
        return roll;
    }

//    static boolean isRelativelyPrime(Long a, Long b){
//
//        BigInteger bigA = BigInteger.valueOf(a);
//        BigInteger bigB = BigInteger.valueOf(b);
//        return isRelativelyPrime(bigA, bigB);
//    };
}
