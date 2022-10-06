package com.saugier.dbame.core.service;

import com.saugier.dbame.core.model.base.EncryptedBallot;
import com.saugier.dbame.core.model.base.EncryptedBlindFactor;
import com.saugier.dbame.core.model.base.Person;
import com.saugier.dbame.core.model.base.Signature;
import com.saugier.dbame.core.model.web.BallotRelayResponse;
import com.saugier.dbame.core.model.web.RegistrationRequest;
import com.saugier.dbame.core.model.web.RegistrationResponse;
import org.springframework.stereotype.Service;

@Service
public interface IBaseObjectMapper {

    Person map(RegistrationRequest registrationRequest);

    RegistrationResponse map(Person person);

    String[] map(Signature signature);

    Signature map(String[] map);

    String[] map(EncryptedBlindFactor encryptedBlindFactor);

    BallotRelayResponse map(EncryptedBallot encryptedBallot);
}
