package com.saugier.dbame.core.model.web;

/**
 * Message passed from the Registrar to the Moderator.
 * Contains the encrypted ballot and an ephemeral key.
 */
public class BallotRelayResponse {

    private String encryptedBallot;
    private String ephemeralKey;


    public String getEncryptedBallot() {
        return encryptedBallot;
    }

    public void setEncryptedBallot(String encryptedBallot) {
        this.encryptedBallot = encryptedBallot;
    }

    public String getEphemeralKey() {
        return ephemeralKey;
    }

    public void setEphemeralKey(String ephemeralKey) {
        this.ephemeralKey = ephemeralKey;
    }
}
