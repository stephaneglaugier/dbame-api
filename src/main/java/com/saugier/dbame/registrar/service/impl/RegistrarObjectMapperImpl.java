package com.saugier.dbame.registrar.service.impl;

import com.saugier.dbame.core.model.base.Ballot;
import com.saugier.dbame.core.model.base.Person;
import com.saugier.dbame.core.model.base.Roll;
import com.saugier.dbame.core.model.base.Signature;
import com.saugier.dbame.registrar.model.entity.BallotRE;
import com.saugier.dbame.registrar.model.entity.PersonRE;
import com.saugier.dbame.registrar.model.entity.RollRE;
import com.saugier.dbame.registrar.service.IRegistrarObjectMapper;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class RegistrarObjectMapperImpl implements IRegistrarObjectMapper {


    private static final int DEFAULT_RADIX = 16;

    
    public PersonRE map(Person in) {

        PersonRE out = new PersonRE();

        out.setId(in.getId());
        out.setFirstName(in.getFirstName());
        out.setLastName(in.getLastName());
        out.setDob(in.getDob());
        out.setRoll(map(in.getRoll()));

        return out;
    }

    
    public Person map(PersonRE in) {

        Person out = new Person();

        out.setId(in.getId());
        out.setFirstName(in.getFirstName());
        out.setLastName(in.getLastName());
        out.setDob(in.getDob());
        out.setRoll(map(in.getRoll()));

        return out;
    }

    
    public RollRE map(Roll in) {

        RollRE out = new RollRE();

        out.setY(in.getPublicKey().toString(DEFAULT_RADIX));
        if (in.getSignature() != null) {
            out.setW(in.getSignature().getW().toString(DEFAULT_RADIX));
            out.setS(in.getSignature().getS().toString(DEFAULT_RADIX));
        }
        return out;
    }

    
    public Roll map(RollRE in) {

        Roll out = new Roll(
                new BigInteger(in.getY(), DEFAULT_RADIX),
                new Signature(
                        new BigInteger(in.getW(), DEFAULT_RADIX),
                        new BigInteger(in.getS(), DEFAULT_RADIX)
                )
        );
        return out;
    }

    
    public BallotRE map(Ballot in, long permutation) {

        BallotRE out = new BallotRE();

        out.setId(in.getId());
        out.setTimestamp(in.getTimestamp());
        out.setRandint(in.getRandint());
        out.setW(in.getSignature().getW().toString(DEFAULT_RADIX));
        out.setS(in.getSignature().getS().toString(DEFAULT_RADIX));
        out.setPermutation(permutation);

        return out;
    }

    
    public Ballot map(BallotRE in) {

        Ballot out = new Ballot();

        out.setId(in.getId());
        out.setTimestamp(in.getTimestamp());
        out.setRandint(in.getRandint());
        out.setSignature(
                new Signature(
                        new BigInteger(in.getW(), DEFAULT_RADIX),
                        new BigInteger(in.getS(), DEFAULT_RADIX)
                )
        );
        return out;
    }
}
