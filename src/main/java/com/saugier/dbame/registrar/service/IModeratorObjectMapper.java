package com.saugier.dbame.registrar.service;

import com.saugier.dbame.core.model.base.Roll;
import com.saugier.dbame.registrar.model.entity.mysql.RollRE;
import org.springframework.stereotype.Service;

@Service
public interface IModeratorObjectMapper {

    RollRE map(Roll roll);

    Roll map(RollRE rollRE);
}
