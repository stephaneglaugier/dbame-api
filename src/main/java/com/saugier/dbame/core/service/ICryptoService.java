package com.saugier.dbame.core.service;

import com.saugier.dbame.core.model.base.*;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Map;

@Service
public interface ICryptoService {

    BigInteger randomCoprime(BigInteger in);

    BigInteger randomlySelect(BigInteger upperBound) throws Exception;

    Ballot sign (Ballot ballot) throws Exception;

    Roll sign(Roll roll) throws Exception;

    EncryptedBallot encryptBallot(Ballot ballot, BigInteger maskedY, long permutation) throws Exception;

    boolean validate(Roll roll);

    Signature sign (BigInteger bigInteger);

    Mask mask(BigInteger bigInteger);

    EncryptedBlindFactor encrypt(BigInteger blindFactor, BigInteger voterPublicKey);

    Map.Entry<BigInteger, BigInteger> generatePrimeAndGenerator(int bitLength);

    BigInteger generatePrivateKey(BigInteger p, int bitLength);

    }
