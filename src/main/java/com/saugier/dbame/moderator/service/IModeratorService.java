package com.saugier.dbame.moderator.service;

import org.springframework.stereotype.Service;

@Service
public interface IModeratorService {
    String handleRequestBallot(String json) throws Exception;
}
