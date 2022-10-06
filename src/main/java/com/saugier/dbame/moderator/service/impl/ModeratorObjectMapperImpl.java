package com.saugier.dbame.moderator.service.impl;

import com.saugier.dbame.core.model.base.Datum;
import com.saugier.dbame.core.model.base.Mask;
import com.saugier.dbame.core.model.base.Roll;
import com.saugier.dbame.core.model.base.Signature;
import com.saugier.dbame.core.model.base.EncryptedBallot;
import com.saugier.dbame.core.model.base.MaskedRequest;
import com.saugier.dbame.moderator.model.entity.ModeratorRelayME;
import com.saugier.dbame.moderator.service.IModeratorObjectMapper;
import com.saugier.dbame.registrar.model.entity.RollRE;
import org.springframework.stereotype.Service;

@Service
public class ModeratorObjectMapperImpl implements IModeratorObjectMapper {

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
    public ModeratorRelayME map(EncryptedBallot ec, MaskedRequest mr, Roll roll, Mask mask) {

        ModeratorRelayME out = new ModeratorRelayME();

        out.setY(roll.getPublicKey().toString());
        out.setMaskedY(mr.getMaskedData().toString());
        out.setW(roll.getSignature().getW().toString());
        out.setS(roll.getSignature().getS().toString());
        out.setPermutation(mr.getPermutation());
        out.setBlindFactor(mask.getMask().toString());
        out.setCypherText(ec.getCypherText().toString());
        out.setEphemeralKey(ec.getEphemeralKey().toString());

        return out;
    }
}
