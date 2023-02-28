package com.saugier.dbame.registrar.service.impl;

import com.google.gson.Gson;
import com.saugier.dbame.core.exception.InvalidSignatureException;
import com.saugier.dbame.core.model.base.EncryptedBlindFactor;
import com.saugier.dbame.core.model.base.Mask;
import com.saugier.dbame.core.model.base.Person;
import com.saugier.dbame.core.model.web.BallotRelayRequest;
import com.saugier.dbame.core.model.web.BallotRelayResponse;
import com.saugier.dbame.core.model.web.BallotRequest;
import com.saugier.dbame.core.model.web.BallotResponse;
import com.saugier.dbame.core.service.IBaseObjectMapper;
import com.saugier.dbame.core.service.ICryptoService;
import com.saugier.dbame.registrar.model.entity.h2.ModeratorRelayME;
import com.saugier.dbame.registrar.model.entity.h2.PermutationME;
import com.saugier.dbame.registrar.repository.h2.IEncryptedBallotDAO;
import com.saugier.dbame.registrar.repository.h2.IPermutationDAO;
import com.saugier.dbame.registrar.service.IModeratorObjectMapper;
import com.saugier.dbame.registrar.service.IModeratorService;
import com.saugier.dbame.registrar.model.entity.mysql.RollRE;
import com.saugier.dbame.registrar.repository.mysql.IRollDAO;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ModeratorServiceImpl implements IModeratorService {

    private static final int DEFAULT_RADIX = 16;

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

    @Autowired
    IPermutationDAO permutationDAO;

    @Autowired
    IModeratorObjectMapper moderatorObjectMapper;

    @Autowired
    IBaseObjectMapper baseObjectMapper;

    
    public BallotResponse handleRequestBallot(BallotRequest ballotRequest) throws Exception {

        Person person = baseObjectMapper.map(ballotRequest);

        RollRE rollRE = moderatorObjectMapper.map(person.getRoll());

        Optional<RollRE> record = rollDAO.findByY(rollRE.getY());
        if (!record.isPresent()){
            throw new Exception("public key not found in electoral roll");
        }
        long id = record.get().getId();

        if(cryptoService.validate(person.getRoll())){

            Mask mask = cryptoService.mask(person.getRoll().getPublicKey());
            BallotRelayRequest ballotRelayRequest = new BallotRelayRequest();
            ballotRelayRequest.setMask(mask.getMask().toString(DEFAULT_RADIX));
            ballotRelayRequest.setPermutation(permutationDAO.findByFrom(id).get().getTo());
            String body = gson.toJson(ballotRelayRequest);
            log.warn(String.format("Sending masked ballot request to registrar: %s", body));
            ResponseEntity<String> response =
                    new RestTemplate().postForEntity("http://localhost:8080/registrar/requestBallot", body,  String.class);
            BallotRelayResponse ballotRelayResponse = gson.fromJson(response.getBody(), BallotRelayResponse.class);


            // Save all important data under one object for now
            ModeratorRelayME moderatorRelayME = new ModeratorRelayME();
            moderatorRelayME.setY(ballotRequest.getPublicKey());
            moderatorRelayME.setMaskedY(ballotRelayRequest.getMask());
            moderatorRelayME.setW(ballotRequest.getW());
            moderatorRelayME.setS(ballotRequest.getS());
            moderatorRelayME.setPermutation(ballotRelayRequest.getPermutation());
            moderatorRelayME.setBlindFactor(mask.getBlindFactor().toString(DEFAULT_RADIX));
            moderatorRelayME.setCypherText(ballotRelayResponse.getEncryptedBallot());
            moderatorRelayME.setEphemeralKey(ballotRelayResponse.getEphemeralKey());
            encryptedBallotDAO.save(moderatorRelayME);

            EncryptedBlindFactor encryptedBlindFactor = cryptoService.encrypt(mask.getBlindFactor(), person.getRoll().getPublicKey());

            BallotResponse out = new BallotResponse();
            out.setEncryptedBallot(ballotRelayResponse.getEncryptedBallot());
            out.setEphemeralKey(ballotRelayResponse.getEphemeralKey());
            out.setEncryptedBlindFactor(baseObjectMapper.map(encryptedBlindFactor));

            log.warn("bi: " + mask.getBlindFactor().toString(DEFAULT_RADIX));
            return out;
        } else {
            throw new InvalidSignatureException("Roll signature was invalid.");
        }
    }

    
    public String handleGeneratePermutation() throws Exception {
        ResponseEntity<Long> response =
                new RestTemplate().getForEntity("http://localhost:8080/registrar/getNRolls", Long.class);
        long n = response.getBody();
        PermutationME permutationME;
        List<PermutationME> out = new ArrayList<>();
        for (Long i = new Long(1);i<=n;i++){
            permutationME = new PermutationME();
            permutationME.setFrom(i);
            out.add(permutationME);
        }
        Collections.shuffle(out);
        for (int l = out.size();l<=n;l++){
        out.get(l).setTo(l);
        }
        permutationDAO.deleteAll();
        permutationDAO.saveAll(out);
        return "Permutation generated successfully";
    }
}