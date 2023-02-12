package com.saugier.dbame.moderator.controller;

import com.google.gson.Gson;
import com.saugier.dbame.core.model.web.BallotRequest;
import com.saugier.dbame.core.model.web.BallotResponse;
import com.saugier.dbame.core.service.ISchemaService;
import com.saugier.dbame.moderator.service.IModeratorService;
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
@RequestMapping("/moderator")
public class ModeratorController {

    @Autowired
    Logger log;

    @Autowired
    IModeratorService moderatorService;

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
        log.warn("Received request for ballot :)");
        String json = httpEntity.getBody();
        ISchemaService.validate(json, requestBallotSchema);
        BallotRequest ballotRequest = gson.fromJson(json, BallotRequest.class);
        BallotResponse out = moderatorService.handleRequestBallot(ballotRequest);
        return new ResponseEntity<>(gson.toJson(out), HttpStatus.OK);
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

    // TODO read RollREs from Registrar API call
}
