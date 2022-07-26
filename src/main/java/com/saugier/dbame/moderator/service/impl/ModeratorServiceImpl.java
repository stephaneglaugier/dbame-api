package com.saugier.dbame.moderator.service.impl;

import com.google.gson.Gson;
import com.saugier.dbame.core.exception.InvalidSignatureException;
import com.saugier.dbame.core.model.BallotRequest;
import com.saugier.dbame.core.model.BallotResponse;
import com.saugier.dbame.core.model.entity.Roll;
import com.saugier.dbame.core.repository.IRollDAO;
import com.saugier.dbame.core.service.ICryptoService;
import com.saugier.dbame.moderator.model.entity.EncryptedBallot;
import com.saugier.dbame.moderator.repository.IEncryptedBallotDAO;
import com.saugier.dbame.moderator.service.IModeratorService;
import com.sun.org.slf4j.internal.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
    IEncryptedBallotDAO encryptedBallotDAO;

    @Override
    public String handleRequestBallot(String json) throws Exception {
        Roll roll = gson.fromJson(json, Roll.class);

        // TODO check that voter is in electoral roll
        Optional<Roll> record = rollDAO.findByY(roll.getY());
        if (!record.isPresent()){
            return "public key not found in electoral roll";
        }
        String unmaskedY = roll.getY();
        if(roll.equals(record.get())){
            roll = record.get();
        }

        if(cryptoService.validate(roll)){

            EncryptedBallot encryptedBallot = cryptoService.encrypt(roll);




            // TODO relay encrypted roll to Registrar
            BallotRequest ballotRequest = new BallotRequest();
            ballotRequest.setMask(encryptedBallot.getRoll().getY());
            ballotRequest.setPermutation(1);
            String body = gson.toJson(ballotRequest);
            log.warn(String.format("Sending masked ballot request to registrar: %s", body));
            ResponseEntity<String> response =
                    new RestTemplate().postForEntity("http://localhost:8080/registrar/requestBallot",body,  String.class);

            BallotResponse ballotResponse = gson.fromJson(response.getBody(), BallotResponse.class);
            encryptedBallot.setCypherText(ballotResponse.getCiphertext());
            encryptedBallot.setEphemeralKey(ballotResponse.getEphemeralKey());
            encryptedBallot.getRoll().setId(null);
            encryptedBallot.getRoll().setY(unmaskedY);

            // TODO insert in DB
            encryptedBallotDAO.save(encryptedBallot);

            return gson.toJson(encryptedBallot);
        } else {
            throw new InvalidSignatureException("Roll signature was invalid.");
        }
    }
}
