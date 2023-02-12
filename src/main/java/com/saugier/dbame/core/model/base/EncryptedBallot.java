package com.saugier.dbame.core.model.base;

/**
 * Class representing a d-BAME encrypted ballot.
 *
 * Sent by the Registrar to the Moderator following a ballot request.
 */
public class EncryptedBallot {

    private String cypherText;
    private Datum ephemeralKey;

    public String getCypherText() {
        return cypherText;
    }

    public void setCypherText(String cypherText) {
        this.cypherText = cypherText;
    }

    public Datum getEphemeralKey() {
        return ephemeralKey;
    }

    public void setEphemeralKey(Datum ephemeralKey) {
        this.ephemeralKey = ephemeralKey;
    }
}

