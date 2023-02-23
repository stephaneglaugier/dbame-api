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

    @Override
    public String getDbameVersion() {
        return dbameVersion;
    }

    @Override
    public BigInteger getP() {
        return new BigInteger(p, DEFAULT_RADIX);
    }

    @Override
    public BigInteger getG() {
        return new BigInteger(g, DEFAULT_RADIX);
    }

    @Override
    public byte[] getIv() throws DecoderException {
        return Hex.decodeHex(iv);
    }

    @Override
    public List<String> getCandidates() {
        return candidates;
    }

    @Override
    public ElectionParams asElectionParams() throws DecoderException {
        return new ElectionParams(
                getDbameVersion(),
                getP(),
                getG(),
                getIv(),
                getCandidates()
        );
    }
}

