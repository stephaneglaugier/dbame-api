package com.saugier.dbame.core.model.web;

/**
 * Response passed from the Moderator to the Voter after a ballot request.
 */
public class BallotResponse extends BallotRelayResponse{

    private String[] encryptedBlindFactor;

    public String[] getEncryptedBlindFactor() {
        return encryptedBlindFactor;
    }

    public void setEncryptedBlindFactor(String[] encryptedBlindFactor) {
        this.encryptedBlindFactor = encryptedBlindFactor;
    }
}
