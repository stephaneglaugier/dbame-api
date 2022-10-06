package com.saugier.dbame.core.model.base;

/**
 * Class representing a d-BAME encrypted ballot.
 *
 * Sent by the Registrar to the Moderator following a ballot request.
 */
public class EncryptedBallot {

    private Datum cypherText;
    private Datum ephemeralKey;

    public Datum getCypherText() {
        return cypherText;
    }

    public void setCypherText(Datum cypherText) {
        this.cypherText = cypherText;
    }

    public Datum getEphemeralKey() {
        return ephemeralKey;
    }

    public void setEphemeralKey(Datum ephemeralKey) {
        this.ephemeralKey = ephemeralKey;
    }
}

