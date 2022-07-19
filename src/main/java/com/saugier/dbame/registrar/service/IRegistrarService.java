package com.saugier.dbame.registrar.service;

import org.springframework.stereotype.Service;

@Service
public interface IRegistrarService {

    String handleRegisterToVote(String body) throws Exception;
    String handleGenerateBallots();
    String handleRequestBallot(String body);
}
