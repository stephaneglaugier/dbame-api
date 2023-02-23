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


    public ElectionParams(
            String dbameVersion, String p, String g, String iv,
            String[] candidates) {
        this.dbameVersion = dbameVersion;
        this.p = p;
        this.g = g;
        this.iv = iv;
        this.candidates = candidates;
    }

    public ElectionParams(
            String dbameVersion, BigInteger p, BigInteger g, byte[] iv,
            List<String> candidates) {
        this.dbameVersion = dbameVersion;
        this.p = p.toString(DEFAULT_RADIX);
        this.g = g.toString(DEFAULT_RADIX);
        this.iv = Hex.encodeHexString(iv);
        this.candidates = candidates.toArray(new String[0]);
    }


    public String getDbameVersion() {
        return dbameVersion;
    }

    public String getP() {
        return p;
    }

    public String getG() {
        return g;
    }

    public String getIv() {
        return iv;
    }


    public String[] getCandidates() {
        return candidates;
    }
}
