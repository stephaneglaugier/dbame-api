package com.saugier.dbame.core.service.impl;

import com.saugier.dbame.core.model.base.*;
import com.saugier.dbame.core.model.web.*;
import com.saugier.dbame.core.service.IBaseObjectMapper;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class BaseObjectMapperImpl implements IBaseObjectMapper {

    private static final int DEFAULT_RADIX = 16;

    
    public Person map(RegistrationRequest in) {

        Person out = new Person();

        out.setId(in.getId());
        out.setFirstName(in.getFirstName());
        out.setLastName(in.getLastName());
        out.setDob(in.getDob());
        out.setRoll(new Roll(new BigInteger(in.getPublicKey(), DEFAULT_RADIX), null));

        return out;
    }

    
    public Person map(BallotRequest in) {
        Person out = new Person();

        out.setId(in.getId());
        out.setFirstName(in.getFirstName());
        out.setLastName(in.getLastName());
        out.setDob(in.getDob());
        out.setRoll(
                new Roll(
                        new BigInteger(in.getPublicKey(), DEFAULT_RADIX),
                        new Signature(
                                new BigInteger(in.getW(), DEFAULT_RADIX),
                                new BigInteger(in.getS(), DEFAULT_RADIX)
                        )
                )
        );
        return out;
    }

    
    public RegistrationResponse map(Person in) {

        RegistrationResponse out = new RegistrationResponse();

        out.setId(in.getId());
        out.setFirstName(in.getFirstName());
        out.setLastName(in.getLastName());
        out.setDob(in.getDob());
        out.setPublicKey(in.getRoll().getPublicKey().toString(DEFAULT_RADIX));
        out.setW(in.getRoll().getSignature().getW().toString(DEFAULT_RADIX));
        out.setS(in.getRoll().getSignature().getS().toString(DEFAULT_RADIX));

        return out;
    }

    
    public String[] map(Signature in) {
        return new String[]{
                in.getW().toString(DEFAULT_RADIX),
                in.getS().toString(DEFAULT_RADIX)
        };
    }

    
    public Signature map(String[] in) {
        return new Signature(
                new BigInteger(in[0], DEFAULT_RADIX),
                new BigInteger(in[1], DEFAULT_RADIX)
        );
    }

    
    public String[] map(EncryptedBlindFactor in) {
        return new String[]{
                in.getC1().toString(DEFAULT_RADIX),
                in.getC2().toString(DEFAULT_RADIX)
        };
    }

    
    public BallotRelayResponse map(EncryptedBallot in) {

        BallotRelayResponse out = new BallotRelayResponse();

        out.setEncryptedBallot(in.getCypherText());
        out.setEphemeralKey(in.getEphemeralKey().toString(DEFAULT_RADIX));

        return out;
    }

    
    public MaskedRequest map(BallotRelayRequest in) {

        MaskedRequest out = new MaskedRequest(
                new BigInteger(in.getMask(), DEFAULT_RADIX),
                in.getPermutation()
        );

        return out;
    }
}
