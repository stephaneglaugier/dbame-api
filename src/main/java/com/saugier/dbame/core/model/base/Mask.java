package com.saugier.dbame.core.model.base;

import java.math.BigInteger;

/**
 * Class representing a d-BAME mask.
 *
 * Used by the Moderator to mask a Voter's ballot request.
 */
public class Mask {

    private BigInteger mask;
    private BigInteger blindFactor;

    public Mask(BigInteger mask, BigInteger blindFactor){
        this.setMask(mask);
        this.setBlindFactor(blindFactor);
    }

    public BigInteger getMask() {
        return mask;
    }

    public void setMask(BigInteger mask) {
        this.mask = mask;
    }

    public BigInteger getBlindFactor() {
        return blindFactor;
    }

    public void setBlindFactor(BigInteger blindFactor) {
        this.blindFactor = blindFactor;
    }
}
