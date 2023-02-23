package com.saugier.dbame.core.service;

import com.saugier.dbame.core.model.web.ElectionParams;
import org.apache.commons.codec.DecoderException;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public interface IElectionService {

    public String getDbameVersion();

    public BigInteger getP();

    public BigInteger getG();

    public byte[] getIv() throws DecoderException;

    public List<String> getCandidates();

    public ElectionParams asElectionParams() throws DecoderException;
}

