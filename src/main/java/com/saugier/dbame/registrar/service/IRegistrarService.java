package com.saugier.dbame.registrar.service;

import com.saugier.dbame.core.model.web.BallotRelayRequest;
import com.saugier.dbame.core.model.web.BallotRelayResponse;
import com.saugier.dbame.core.model.web.RegistrationRequest;
import com.saugier.dbame.core.model.web.RegistrationResponse;
import org.springframework.stereotype.Service;

@Service
public interface IRegistrarService {

    RegistrationResponse handleRegisterToVote(RegistrationRequest body) throws Exception;
    String handleGenerateBallots() throws Exception;
    BallotRelayResponse handleRequestBallot(BallotRelayRequest ballotRelayRequest) throws Exception;
}
