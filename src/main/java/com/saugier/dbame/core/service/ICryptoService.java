package com.saugier.dbame.core.service;

import com.saugier.dbame.core.model.base.*;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public interface ICryptoService {

    /**
     * Return a random coprime BigInteger
     * @param in
     * @return
     */
    BigInteger randomCoprime(BigInteger in);

    /**
     * Select a random BigInteger given an upper bound.
     *
     * @param upperBound
     * @throws Exception
     */
    BigInteger randomlySelect(BigInteger upperBound) throws Exception;

    /**
     * Signs the given ballot, returning a Ballot object.
     *
     * @param ballot
     * @throws Exception
     */
    Ballot sign (Ballot ballot) throws Exception;

    /**
     * Signs the given rollRE.
     *
     * @param roll
     * @throws Exception
     */
    Roll sign(Roll roll) throws Exception;

    /**
     *
     * @param ballot
     * @param maskedY,
     * @param permutation
     * @return cypher and ephemeral key
     * @throws Exception
     */
    EncryptedBallot encryptBallot(Ballot ballot, BigInteger maskedY, long permutation) throws Exception;

    /**
     * Validates that a rollRE's El-Gamal Signature is valid
     * @param roll
     * @return
     */
    boolean validate(Roll roll);

    /**
     * Signs a BigInteger using El-Gamal signature scheme.
     * @param bigInteger
     * @return
     */
    Signature sign (BigInteger bigInteger);

    /**
     * Masks a given bigInteger, returning the blind factor used.
     * @param bigInteger
     * @return
     */
    Mask mask(BigInteger bigInteger);

    /**
     * Encrypts a blind factor
     * @param blindFactor
     * @return
     */
    EncryptedBlindFactor encrypt(BigInteger blindFactor, BigInteger voterPublicKey);

}
