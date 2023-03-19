package com.saugier.dbame.registrar.controller;

import com.google.gson.Gson;
import com.saugier.dbame.core.model.web.BallotRequest;
import com.saugier.dbame.core.model.web.BallotResponse;
import com.saugier.dbame.core.service.IElectionService;
import com.saugier.dbame.core.service.ISchemaService;
import com.saugier.dbame.registrar.service.IModeratorService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/moderator")
@ConditionalOnProperty(name = "user.is.moderator", havingValue = "true", matchIfMissing = false)
public class ModeratorController {

    @Autowired
    Logger log;

    @Autowired
    IModeratorService moderatorService;

    @Autowired
    IElectionService electionService;

    @Autowired
    Gson gson;

    @Value("${schemas.moderator.requestBallot}")
    private String requestBallotSchema;

    @RequestMapping(
            value = "/requestBallot",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> requestBallot(HttpEntity<String> httpEntity) throws Exception {
        if (electionService.getElectionState().equalsIgnoreCase("voting")){
            log.warn("Received request for ballot :)");
            String json = httpEntity.getBody();
            ISchemaService.validate(json, requestBallotSchema);
            BallotRequest ballotRequest = gson.fromJson(json, BallotRequest.class);
            BallotResponse out = moderatorService.handleRequestBallot(ballotRequest);
            return new ResponseEntity<>(gson.toJson(out), HttpStatus.OK);
        } else {
            String errorMessage = "{\"error\": \"Service is currently unavailable\"}";
            return new ResponseEntity<>(errorMessage, HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @RequestMapping(
            value = "/generatePermutation",
            method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> generatePermutation(HttpEntity<String> httpEntity) throws Exception {
        log.warn("Received request permutation generation");
        String out = moderatorService.handleGeneratePermutation();
        return new ResponseEntity<>(out, HttpStatus.OK);
    }
}
