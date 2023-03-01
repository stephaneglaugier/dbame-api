package com.saugier.dbame.registrar.service;

import com.saugier.dbame.core.model.web.*;
import org.apache.commons.codec.DecoderException;
import org.springframework.stereotype.Service;

@Service
public interface IRegistrarService {

    RegistrationResponse handleRegisterToVote(RegistrationRequest body) throws Exception;

    String handleGenerateBallots() throws Exception;

    BallotRelayResponse handleRequestBallot(BallotRelayRequest ballotRelayRequest) throws Exception;

    ElectionParams handleElectionParams() throws DecoderException, Exception;

}
