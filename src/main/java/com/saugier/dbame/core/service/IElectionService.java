package com.saugier.dbame.core.service;

import com.saugier.dbame.core.model.web.ElectionParams;
import org.apache.commons.codec.DecoderException;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public interface IElectionService {

    String getDbameVersion();

    BigInteger getP();

    BigInteger getG();

    byte[] getIv() throws DecoderException;

    List<String> getCandidates();

    String getContractAddress();

    String getContractNetwork();

    String getElectionState();

    ElectionParams asElectionParams() throws DecoderException;
}

