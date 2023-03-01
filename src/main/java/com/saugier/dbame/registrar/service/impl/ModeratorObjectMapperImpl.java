package com.saugier.dbame.registrar.service.impl;

import com.saugier.dbame.core.model.base.*;
import com.saugier.dbame.registrar.service.IModeratorObjectMapper;
import com.saugier.dbame.registrar.model.entity.mysql.RollRE;
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

}
