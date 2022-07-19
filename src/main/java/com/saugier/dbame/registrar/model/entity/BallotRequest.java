package com.saugier.dbame.registrar.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class BallotRequest {

    @Id
    private long permutation;

    @Column
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
