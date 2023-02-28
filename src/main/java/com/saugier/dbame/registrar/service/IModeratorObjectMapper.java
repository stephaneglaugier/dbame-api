package com.saugier.dbame.registrar.service;

import com.saugier.dbame.core.model.base.Mask;
import com.saugier.dbame.core.model.base.Roll;
import com.saugier.dbame.core.model.base.EncryptedBallot;
import com.saugier.dbame.core.model.base.MaskedRequest;
import com.saugier.dbame.registrar.model.entity.h2.ModeratorRelayME;
import com.saugier.dbame.registrar.model.entity.mysql.RollRE;
import org.springframework.stereotype.Service;

@Service
public interface IModeratorObjectMapper {

    RollRE map(Roll roll);

    Roll map(RollRE rollRE);

    ModeratorRelayME map(EncryptedBallot ec, MaskedRequest mr, Roll roll, Mask mask);
}
