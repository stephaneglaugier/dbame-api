package com.saugier.dbame.registrar.service;

import org.springframework.stereotype.Service;

@Service
public interface IRegistrarService {
    public String handleRegisterToVote(String body) throws Exception;
}
