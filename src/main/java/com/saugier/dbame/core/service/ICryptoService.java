package com.saugier.dbame.core.service;

import com.saugier.dbame.registrar.model.Ballot;
import com.saugier.dbame.registrar.model.BallotResponse;
import com.saugier.dbame.registrar.model.entity.BallotRequest;
import com.saugier.dbame.registrar.model.entity.SignedBallot;
import com.saugier.dbame.registrar.model.entity.Roll;
import org.springframework.stereotype.Service;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
public interface ICryptoService {

    boolean isRelativelyPrime(BigInteger a, BigInteger b);

    BigInteger randomlySelect(BigInteger upperBound);

    BigInteger randomCoprime(BigInteger in);

    BigInteger computePublicKey();

    Roll EGSignRoll(final Roll roll);

    Roll encryptRoll(Roll roll);

    SecretKey generateKey() throws NoSuchAlgorithmException;

    SealedObject encryptObject(String algorithm, Serializable object,
                                             SecretKey key, IvParameterSpec iv) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidAlgorithmParameterException,
            InvalidKeyException, IOException, IllegalBlockSizeException;

    Serializable decryptObject(String algorithm, SealedObject sealedObject,
                                             SecretKey key, IvParameterSpec iv) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException,
            ClassNotFoundException, BadPaddingException, IllegalBlockSizeException,
            IOException;

    SignedBallot sign (Ballot b);

    Object EGSign (String message);

    BallotResponse encryptBallot(SignedBallot signedBallot, BallotRequest ballotRequest) throws Exception;
}
