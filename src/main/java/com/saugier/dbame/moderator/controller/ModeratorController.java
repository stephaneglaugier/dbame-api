package com.saugier.dbame.moderator.controller;

import com.saugier.dbame.moderator.service.IModeratorService;
import com.sun.org.slf4j.internal.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("moderator")
public class ModeratorController {

    @Autowired
    Logger log;

    @Autowired
    IModeratorService moderatorService;

//    @Value("${schemas.moderator.requestBallot}")
//    private String requestBallotSchema;

    @RequestMapping(
            value = "/requestBallot",
            method = RequestMethod.GET,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> requestBallot(HttpEntity<String> httpEntity) throws Exception {
        log.warn("Received request for ballot");
        String json = httpEntity.getBody();

        // TODO validate schema

        String out = moderatorService.handleRequestBallot(json);
        return new ResponseEntity<>(out, HttpStatus.OK);
    }
}
