package com.saugier.dbame.registrar.service;

import com.saugier.dbame.core.model.web.BallotRequest;
import com.saugier.dbame.core.model.web.BallotResponse;
import org.springframework.stereotype.Service;

@Service
public interface IModeratorService {
    BallotResponse handleRequestBallot(BallotRequest ballotRequest) throws Exception;
    String handleGeneratePermutation() throws Exception;
}
