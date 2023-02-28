package com.saugier.dbame.moderator.service.impl;

import com.saugier.dbame.core.model.base.*;
import com.saugier.dbame.moderator.model.entity.ModeratorRelayME;
import com.saugier.dbame.moderator.service.IModeratorObjectMapper;
import com.saugier.dbame.core.model.entity.RollRE;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class ModeratorObjectMapperImpl implements IModeratorObjectMapper {

    private static final int DEFAULT_RADIX = 16;

    
    public RollRE map(Roll in) {
        RollRE out = new RollRE();
        out.setY(in.getPublicKey().toString(DEFAULT_RADIX));
        out.setW(in.getSignature().getW().toString(DEFAULT_RADIX));
        out.setS(in.getSignature().getS().toString(DEFAULT_RADIX));
        return out;
    }

    
    public Roll map(RollRE in) {

        Roll out = new Roll(
                new BigInteger(in.getY()),
                new Signature(
                        new BigInteger(in.getW(), DEFAULT_RADIX),
                        new BigInteger(in.getS(), DEFAULT_RADIX)
                )
        );
        return out;
    }

    
    public ModeratorRelayME map(EncryptedBallot ec, MaskedRequest mr, Roll roll, Mask mask) {

        ModeratorRelayME out = new ModeratorRelayME();

        out.setY(roll.getPublicKey().toString(DEFAULT_RADIX));
        out.setMaskedY(mr.getMaskedData().toString(DEFAULT_RADIX));
        out.setW(roll.getSignature().getW().toString(DEFAULT_RADIX));
        out.setS(roll.getSignature().getS().toString(DEFAULT_RADIX));
        out.setPermutation(mr.getPermutation());
        out.setBlindFactor(mask.getMask().toString(DEFAULT_RADIX));
        out.setCypherText(ec.getCypherText());
        out.setEphemeralKey(ec.getEphemeralKey().toString(DEFAULT_RADIX));

        return out;
    }
}
