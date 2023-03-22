package com.saugier.dbame.core.service.impl;

import com.saugier.dbame.core.model.web.ElectionParams;
import com.saugier.dbame.core.service.IElectionService;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public class ElectionServiceImpl implements IElectionService {

    private static final int DEFAULT_RADIX = 16;

    @Value("${global.dbameVersion}")
    private String dbameVersion;

    @Value("${global.p}")
    private String p;

    @Value("${global.g}")
    private String g;

    @Value("${global.iv}")
    private String iv;

    @Value("#{'${global.candidates}'.split(',')}")
    private List<String> candidates;

    @Value("${global.contract.address}")
    private String contractAddress;

    @Value("${global.contract.network}")
    private String contractNetwork;

    @Value("${global.node.url}")
    private String votingNode;

    @Value("${global.client.url}")
    private String votingClient;

    @Value("${global.election.state}")
    private String electionState;

    @Value("${registrar.key.public}")
    private String yR;

    @Value("${moderator.key.public}")
    private String yM;
    
    public String getDbameVersion() {
        return dbameVersion;
    }

    
    public BigInteger getP() {
        return new BigInteger(p, DEFAULT_RADIX);
    }

    
    public BigInteger getG() {
        return new BigInteger(g, DEFAULT_RADIX);
    }

    
    public byte[] getIv() throws DecoderException {
        return Hex.decodeHex(iv);
    }

    
    public List<String> getCandidates() {
        return candidates;
    }

    public String getContractAddress() {
        return contractAddress;
    }

    public String getContractNetwork() {
        return contractNetwork;
    }

    public String getVotingNode() {
        return votingNode;
    }

    public String getVotingClient() {
        return votingClient;
    }

    public String getElectionState() {
        return electionState;
    }

    public String getyR() {
        return yR;
    }

    public String getyM() {
        return yM;
    }

    public ElectionParams asElectionParams() throws DecoderException {
        return new ElectionParams(
                getDbameVersion(),
                getP(),
                getG(),
                getIv(),
                getCandidates(),
                getContractAddress(),
                getContractNetwork(),
                getVotingNode(),
                getVotingClient(),
                getElectionState(),
                getyR(),
                getyM()
        );
    }
}

