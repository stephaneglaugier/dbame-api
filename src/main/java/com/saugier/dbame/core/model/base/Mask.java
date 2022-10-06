package com.saugier.dbame.core.model.base;

import java.math.BigInteger;

/**
 * Class representing a d-BAME mask.
 *
 * Used by the Moderator to mask a Voter's ballot request.
 */
public class Mask {

    private Datum mask;
    private Datum blindFactor;

    public Mask(){
        this.mask = new Datum();
        this.blindFactor = new Datum();
    }

    public Mask(Datum mask, Datum permutation){
        this();
        this.setMask(mask);
        this.setBlindFactor(blindFactor);
    }

    public Mask(String mask, String permutation){
        this(new Datum(mask), new Datum(permutation));
    }

    public Mask(BigInteger mask, BigInteger permutation){
        this(new Datum(mask), new Datum(permutation));
    }

    public Mask(byte[] mask, byte[] permutation){
        this(new Datum(mask), new Datum(permutation));
    }

    public Datum getMask() {
        return mask;
    }

    public void setMask(Datum mask) {
        this.mask = mask;
    }

    public Datum getBlindFactor() {
        return blindFactor;
    }

    public void setBlindFactor(Datum blindFactor) {
        this.blindFactor = blindFactor;
    }
}
