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
import com.saugier.dbame.registrar.exception.AlreadyGeneratedException;
import com.saugier.dbame.registrar.exception.PublicKeyNotFoundException;
import com.saugier.dbame.registrar.model.entity.h2.PermutationME;
import com.saugier.dbame.registrar.repository.h2.IPermutationDAO;
import com.saugier.dbame.registrar.service.IModeratorObjectMapper;
import com.saugier.dbame.registrar.service.IModeratorService;
import com.saugier.dbame.registrar.model.entity.mysql.RollRE;
import com.saugier.dbame.registrar.repository.mysql.IRollDAO;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
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
    IPermutationDAO permutationDAO;

    @Autowired
    IModeratorObjectMapper moderatorObjectMapper;

    @Autowired
    IBaseObjectMapper baseObjectMapper;

    @Value("${registrar.address.url}")
    private String registrarURL;


    public BallotResponse handleRequestBallot(BallotRequest ballotRequest) throws Exception {
        if (ballotRequest == null) {
            throw new IllegalArgumentException("Ballot request cannot be null");
        }

        Person person = baseObjectMapper.map(ballotRequest);
        RollRE rollRE = moderatorObjectMapper.map(person.getRoll());

        Optional<RollRE> record = rollDAO.findByY(rollRE.getY());
        if (!record.isPresent()) {
            throw new PublicKeyNotFoundException("Public key not found in electoral roll", rollRE.getY());
        }

        long id = record.get().getId();
        if (!cryptoService.validate(person.getRoll())) {
            throw new InvalidSignatureException("Roll signature was invalid.");
        }

        Mask mask = cryptoService.mask(person.getRoll().getPublicKey());
        BallotRelayRequest ballotRelayRequest = new BallotRelayRequest();
        ballotRelayRequest.setMask(mask.getMask().toString(DEFAULT_RADIX));
        ballotRelayRequest.setPermutation(permutationDAO.findById(id).get().getPermutation());
        String body = gson.toJson(ballotRelayRequest);
        log.warn(String.format("Sending masked ballot request to registrar: %s", body));

        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.postForEntity(registrarURL+"/registrar/requestBallot", body, String.class);
            BallotRelayResponse ballotRelayResponse = gson.fromJson(response.getBody(), BallotRelayResponse.class);

            EncryptedBlindFactor encryptedBlindFactor = cryptoService.encrypt(mask.getBlindFactor(), person.getRoll().getPublicKey());

            BallotResponse out = new BallotResponse();
            out.setEncryptedBallot(ballotRelayResponse.getEncryptedBallot());
            out.setEphemeralKey(ballotRelayResponse.getEphemeralKey());
            out.setEncryptedBlindFactor(baseObjectMapper.map(encryptedBlindFactor));

//            log.warn("bi: " + mask.getBlindFactor().toString(DEFAULT_RADIX));
            return out;
        } catch (RestClientException e) {
            log.error("Error communicating with registrar: " + e.getMessage());
            throw new RuntimeException("Error communicating with registrar", e);
        }
    }


    public String handleGeneratePermutation() throws Exception {
        long count;
        if ((count = permutationDAO.count()) > 0){
            throw new AlreadyGeneratedException(String.format("Permutation of length %s already exists.", count));
        }
        Long n = rollDAO.getMaxId();
        if (n == null) {
            throw new Exception("Unable to retrieve number of rolls");
        }
        List<PermutationME> out = new ArrayList<>();
        for (Long i = 1L; i <= n; i++) {
            PermutationME permutationME = new PermutationME();
            permutationME.setPermutation(i);
            out.add(permutationME);
        }
        Collections.shuffle(out);
        for (int i = 0; i < n; i++) {
            PermutationME permutationME = out.get(i);
            permutationME.setId(i+1);
        }
        try {
            permutationDAO.saveAll(out);
            return "Permutation generated successfully";
        } catch (Exception e) {
            throw new Exception("Error saving permutations: " + e.getMessage());
        }
    }
}
