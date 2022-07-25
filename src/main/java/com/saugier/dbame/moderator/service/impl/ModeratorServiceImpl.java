package com.saugier.dbame.moderator.service.impl;

import com.google.gson.Gson;
import com.saugier.dbame.core.exception.InvalidSignatureException;
import com.saugier.dbame.core.model.entity.Roll;
import com.saugier.dbame.core.repository.IPersonDAO;
import com.saugier.dbame.core.repository.IRollDAO;
import com.saugier.dbame.core.service.ICryptoService;
import com.saugier.dbame.moderator.service.IModeratorService;
import com.sun.org.slf4j.internal.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ModeratorServiceImpl implements IModeratorService {

    @Autowired
    Logger log;

    @Autowired
    Gson gson;

    @Autowired
    ICryptoService cryptoService;

    @Autowired
    IRollDAO rollDAO;

    @Autowired
    IPersonDAO personDAO;

    @Override
    public String handleRequestBallot(String json) throws Exception {
        Roll roll = gson.fromJson(json, Roll.class);

        // TODO check that voter is in electoral roll
        Optional<Roll> record = rollDAO.findByY(roll.getY());
        if (!record.isPresent()){
            return "public key not found in electoral roll";
        }

        if(cryptoService.validate(roll)){
            Roll encryptedRoll = cryptoService.encrypt(roll);

            // TODO insert in DB

            // TODO relay encrypted roll to Registrar

            return gson.toJson(encryptedRoll);
        } else {
            throw new InvalidSignatureException("Roll signature was invalid.");
        }
    }
}
