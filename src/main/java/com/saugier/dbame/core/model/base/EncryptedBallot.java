package com.saugier.dbame.core.model.base;

import java.math.BigInteger;

/**
 * Class representing a d-BAME encrypted ballot.
 *
 * Sent by the Registrar to the Moderator following a ballot request.
 */
public class EncryptedBallot {

    private String cypherText;
    private BigInteger ephemeralKey;

    public String getCypherText() {
        return cypherText;
    }

    public void setCypherText(String cypherText) {
        this.cypherText = cypherText;
    }

    public BigInteger getEphemeralKey() {
        return ephemeralKey;
    }

    public void setEphemeralKey(BigInteger ephemeralKey) {
        this.ephemeralKey = ephemeralKey;
    }
}

