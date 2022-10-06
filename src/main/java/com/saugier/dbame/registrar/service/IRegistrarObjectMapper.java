package com.saugier.dbame.registrar.service;

import com.saugier.dbame.core.model.base.Ballot;
import com.saugier.dbame.core.model.base.Person;
import com.saugier.dbame.core.model.base.Roll;
import com.saugier.dbame.registrar.model.entity.BallotRE;
import com.saugier.dbame.registrar.model.entity.PersonRE;
import com.saugier.dbame.registrar.model.entity.RollRE;
import org.springframework.stereotype.Service;

@Service
public interface IRegistrarObjectMapper {

    PersonRE map(Person person);

    Person map(PersonRE personRE);

    RollRE map(Roll roll);

    Roll map(RollRE rollRE);

    BallotRE map(Ballot ballot, long permutation);

    Ballot map(BallotRE signedBallotRE);

}
