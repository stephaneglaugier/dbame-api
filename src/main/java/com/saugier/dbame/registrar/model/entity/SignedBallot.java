package com.saugier.dbame.registrar.model.entity;

import com.saugier.dbame.registrar.model.Ballot;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class SignedBallot extends Ballot {

    @Column
    private String w;

    @Column
    private String s;

    @Column
    private long permutation;

    public String getW() {
        return w;
    }

    public void setW(String w) {
        this.w = w;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public long getPermutation() {
        return permutation;
    }

    public void setPermutation(long permutation) {
        this.permutation = permutation;
    }
}
