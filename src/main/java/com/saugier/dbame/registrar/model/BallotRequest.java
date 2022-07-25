package com.saugier.dbame.registrar.model;

public class BallotRequest {

    private long permutation;
    private String mask;

    public long getPermutation() {
        return permutation;
    }

    public void setPermutation(long permutation) {
        this.permutation = permutation;
    }

    public String getMask() {
        return mask;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }
}
