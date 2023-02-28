package com.saugier.dbame.moderator.service;

import com.saugier.dbame.core.model.base.Mask;
import com.saugier.dbame.core.model.base.Roll;
import com.saugier.dbame.core.model.base.EncryptedBallot;
import com.saugier.dbame.core.model.base.MaskedRequest;
import com.saugier.dbame.moderator.model.entity.ModeratorRelayME;
import com.saugier.dbame.core.model.entity.RollRE;
import org.springframework.stereotype.Service;

@Service
public interface IModeratorObjectMapper {

    RollRE map(Roll roll);

    Roll map(RollRE rollRE);

    ModeratorRelayME map(EncryptedBallot ec, MaskedRequest mr, Roll roll, Mask mask);
}
