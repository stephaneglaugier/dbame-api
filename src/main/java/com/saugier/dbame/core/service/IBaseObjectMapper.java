package com.saugier.dbame.core.service;

import com.saugier.dbame.core.model.base.*;
import com.saugier.dbame.core.model.web.*;
import org.springframework.stereotype.Service;

@Service
public interface IBaseObjectMapper {

    Person map(RegistrationRequest registrationRequest);

    Person map(BallotRequest registrationRequest);

    RegistrationResponse map(Person person);

    String[] map(Signature signature);

    Signature map(String[] map);

    String[] map(EncryptedBlindFactor encryptedBlindFactor);

    BallotRelayResponse map(EncryptedBallot encryptedBallot);

    MaskedRequest map(BallotRelayRequest ballotRelayRequest);
}
