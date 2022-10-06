package com.saugier.dbame.registrar.service.impl;

import com.saugier.dbame.core.model.base.*;
import com.saugier.dbame.registrar.model.entity.BallotRE;
import com.saugier.dbame.registrar.model.entity.PersonRE;
import com.saugier.dbame.registrar.model.entity.RollRE;
import com.saugier.dbame.registrar.service.IRegistrarObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class RegistrarObjectMapperImpl implements IRegistrarObjectMapper {


    @Override
    public PersonRE map(Person in) {

        PersonRE out = new PersonRE();

        out.setId(in.getId());
        out.setFirstName(in.getFirstName());
        out.setLastName(in.getLastName());
        out.setDob(in.getDob());
        out.setRoll(map(in.getRoll()));

        return out;
    }

    @Override
    public Person map(PersonRE in) {

        Person out = new Person();

        out.setId(in.getId());
        out.setFirstName(in.getFirstName());
        out.setLastName(in.getLastName());
        out.setDob(in.getDob());
        out.setRoll(map(in.getRoll()));

        return out;
    }

    @Override
    public RollRE map(Roll in) {

        RollRE out = new RollRE();

        out.setY(in.getPublicKey().toString());
        out.setW(in.getSignature().getW().toString());
        out.setS(in.getSignature().getS().toString());

        return out;
    }

    @Override
    public Roll map(RollRE in) {

        Roll out = new Roll();

        out.setPublicKey(new Datum(in.getY()));
        out.setSignature(new Signature(in.getW(), in.getS()));

        return out;
    }

    @Override
    public BallotRE map(Ballot in, long permutation) {

        BallotRE out = new BallotRE();

        out.setId(in.getId());
        out.setTimestamp(in.getTimestamp());
        out.setRandint(in.getRandint());
        out.setW(in.getSignature().getW().toString());
        out.setS(in.getSignature().getS().toString());
        out.setPermutation(permutation);

        return out;
    }

    @Override
    public Ballot map(BallotRE in) {

        Ballot out = new Ballot();

        out.setId(in.getId());
        out.setTimestamp(in.getTimestamp());
        out.setRandint(in.getRandint());
        out.setSignature(new Signature(in.getW(), in.getS()));

        return out;
    }
}
