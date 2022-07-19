package com.saugier.dbame.registrar.controller;

import com.google.gson.Gson;
import com.saugier.dbame.core.service.ISchemaService;
import com.saugier.dbame.registrar.service.IRegistrarService;
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
    Gson gson;

    @Autowired
    IRegistrarService registrarService;

    @Autowired
    ISchemaService schemaService;

    @Value("${schemas.registrar.registerToVote}")
    private String registerToVoteSchema;

    @RequestMapping(
            value = "/registerToVote",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> registerToVote(HttpEntity<String> httpEntity) throws Exception {
        System.out.println("Received request for registration");
        String json = httpEntity.getBody();

        ISchemaService.validate(json, registerToVoteSchema);

        String out = registrarService.handleRegisterToVote(json);
        return new ResponseEntity<>(out, HttpStatus.OK);
    }

    @RequestMapping(
            value = "/generateBallots",
            method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> generateBallots(HttpEntity<String> httpEntity) throws Exception {
        System.out.println("Received request for ballot generation");
        // TODO validate schema
        registrarService.handleGenerateBallots();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(
            value = "/requestBallot",
            method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> requestBallot(HttpEntity<String> httpEntity) throws Exception {
        System.out.println("Received request for ballot");
        String json = httpEntity.getBody();

        // TODO validate schema

        String out = registrarService.handleRequestBallot(json);
        return new ResponseEntity<>(out, HttpStatus.OK);
    }
}
