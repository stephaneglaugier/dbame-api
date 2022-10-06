package com.saugier.dbame.core.model.web;

/**
 * Message passed from the Moderator to the Registrar.
 * Contains the masked public key of the voter and a permutation.
 */
public class BallotRelayRequest {

    private String mask;
    private long permutation;

    public String getMask() {
        return mask;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }

    public long getPermutation() {
        return permutation;
    }

    public void setPermutation(long permutation) {
        this.permutation = permutation;
    }
}
