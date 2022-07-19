package com.saugier.dbame.core.service;

import com.saugier.dbame.core.model.BallotRequest;
import com.saugier.dbame.core.model.BallotResponse;
import com.saugier.dbame.registrar.model.entity.Ballot;
import com.saugier.dbame.registrar.model.entity.Roll;
import com.saugier.dbame.registrar.model.entity.SignedBallot;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public interface ICryptoService {

    /**
     * Select a random BigInteger given an upper bound.
     *
     * @param upperBound
     * @throws Exception
     */
    BigInteger randomlySelect(BigInteger upperBound) throws Exception;

    /**
     * Signs the given ballot, returning a SignedBallot object.
     *
     * @param ballot
     * @throws Exception
     */
    SignedBallot sign (Ballot ballot) throws Exception;

    /**
     * Signs the given roll.
     *
     * @param roll
     * @throws Exception
     */
    Roll sign(Roll roll) throws Exception;

    /**
     * Encrypts a given roll by masking the y component.
     *
     * @param roll
     * @throws Exception
     */
    Roll encrypt(Roll roll) throws Exception;

    /**
     *
     * @param signedBallot
     * @param ballotRequest
     * @return cypher and ephemeral key
     * @throws Exception
     */
    BallotResponse encryptBallot(SignedBallot signedBallot, BallotRequest ballotRequest) throws Exception;

}
