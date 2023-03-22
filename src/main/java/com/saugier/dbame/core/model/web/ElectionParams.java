package com.saugier.dbame.core.model.web;

import org.apache.commons.codec.binary.Hex;

import java.math.BigInteger;
import java.util.List;

public class ElectionParams {

    private static final int DEFAULT_RADIX = 16;

    private final String dbameVersion;
    private final String p;
    private final String g;
    private final String iv;
    private final String[] candidates;
    private final String contractAddress;
    private final String contractNetwork;
    private final String votingNode;
    private final String votingClient;
    private final String electionState;
    private final String yR;
    private final String yM;

    public ElectionParams(
            String dbameVersion, BigInteger p, BigInteger g, byte[] iv,
            List<String> candidates, String contractAddress, String contractNetwork, String votingNode, String votingClient, String electionState, String yR, String yM) {
        this.dbameVersion = dbameVersion;
        this.p = p.toString(DEFAULT_RADIX);
        this.g = g.toString(DEFAULT_RADIX);
        this.iv = Hex.encodeHexString(iv);
        this.candidates = candidates.toArray(new String[0]);
        this.contractAddress = contractAddress;
        this.contractNetwork = contractNetwork;
        this.votingNode = votingNode;
        this.votingClient = votingClient;
        this.electionState = electionState;
        this.yR = yR;
        this.yM = yM;
    }
}
