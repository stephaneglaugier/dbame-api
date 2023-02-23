package com.saugier.dbame.registrar.controller;

import com.google.gson.Gson;
import com.saugier.dbame.core.model.web.*;
import com.saugier.dbame.core.service.ISchemaService;
import com.saugier.dbame.registrar.service.IRegistrarService;
import com.sun.org.slf4j.internal.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("registrar")
public class RegistrarController {

    @Autowired
    Logger log;

    @Autowired
    Gson gson;

    @Autowired
    IRegistrarService registrarService;

    @Value("${schemas.registrar.registerToVote}")
    private String registerToVoteSchema;

    @Value("${schemas.registrar.requestBallot}")
    private String requestBallotSchema;

    @RequestMapping(
            value = "/electionParams",
            method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> electionParams(HttpEntity<String> httpEntity) throws Exception {
        log.warn("Received request for election parameters");
        ElectionParams out = registrarService.handleElectionParams();
        return new ResponseEntity<>(gson.toJson(out), HttpStatus.OK);
    }

    @RequestMapping(
            value = "/registerToVote",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> registerToVote(HttpEntity<String> httpEntity) throws Exception {
        log.warn("Received request for registration");
        String json = httpEntity.getBody();
        ISchemaService.validate(json, registerToVoteSchema);
        RegistrationRequest registrationRequest = gson.fromJson(json, RegistrationRequest.class);
        RegistrationResponse out = registrarService.handleRegisterToVote(registrationRequest);
        return new ResponseEntity<>(gson.toJson(out), HttpStatus.OK);
    }

    @RequestMapping(
            value = "/generateBallots",
            method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> generateBallots(HttpEntity<String> httpEntity) throws Exception {
        log.warn("Received request for ballot generation");
        String out = registrarService.handleGenerateBallots();
        return new ResponseEntity<>(out, HttpStatus.OK);
    }

    @RequestMapping(
            value = "/requestBallot",
            method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> requestBallot(HttpEntity<String> httpEntity) throws Exception {
        log.warn("Received request for ballot");
        String json = httpEntity.getBody();
        ISchemaService.validate(json, requestBallotSchema);
        BallotRelayRequest ballotRelayRequest = gson.fromJson(json, BallotRelayRequest.class);
        BallotRelayResponse out = registrarService.handleRequestBallot(ballotRelayRequest);
        return new ResponseEntity<>(gson.toJson(out), HttpStatus.OK);
    }
}
