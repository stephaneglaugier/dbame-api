package com.saugier.dbame.core.service.impl;

import com.saugier.dbame.core.model.base.*;
import com.saugier.dbame.core.model.web.BallotRelayResponse;
import com.saugier.dbame.core.model.web.RegistrationRequest;
import com.saugier.dbame.core.model.web.RegistrationResponse;
import com.saugier.dbame.core.service.IBaseObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class BaseObjectMapperImpl implements IBaseObjectMapper {

    @Override
    public Person map(RegistrationRequest in) {

        Person out = new Person();

        out.setId(in.getId());
        out.setFirstName(in.getFirstName());
        out.setLastName(in.getLastName());
        out.setDob(in.getDob());
        out.setRoll(new Roll(in.getPublicKey(), new Signature(in.getW(), in.getS())));

        return out;
    }

    @Override
    public RegistrationResponse map(Person in) {

        RegistrationResponse out = new RegistrationResponse();

        out.setId(in.getId());
        out.setFirstName(in.getFirstName());
        out.setLastName(in.getLastName());
        out.setDob(in.getDob());
        out.setPublicKey(in.getRoll().getPublicKey().toString());
        out.setW(in.getRoll().getSignature().getW().toString());
        out.setS(in.getRoll().getSignature().getS().toString());

        return out;
    }

    @Override
    public String[] map(Signature in) {
        return new String[]{in.getW().toString(), in.getS().toString()};
    }

    @Override
    public Signature map(String[] in) {
        return new Signature(in[0], in[1]);
    }

    @Override
    public String[] map(EncryptedBlindFactor in) {
        return new String[]{in.getC1().toString(), in.getC2().toString()};
    }

    @Override
    public BallotRelayResponse map(EncryptedBallot in) {

        BallotRelayResponse out = new BallotRelayResponse();

        out.setEncryptedBallot(in.getCypherText().toString());
        out.setEphemeralKey(in.getEphemeralKey().toString());

        return out;
    }
}
