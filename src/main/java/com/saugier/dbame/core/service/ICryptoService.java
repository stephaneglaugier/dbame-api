package com.saugier.dbame.core.service;

import com.saugier.dbame.registrar.model.entity.Roll;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public interface ICryptoService {

    boolean isRelativelyPrime(BigInteger a, BigInteger b);

    long randomlySelect(long upperBound);

    BigInteger randomlySelect(BigInteger upperBound);

    BigInteger randomCoprime(BigInteger in);

    BigInteger computePublicKey();

    Roll EGSignRoll(final Roll roll);

    Roll encryptRoll(Roll roll);
}
