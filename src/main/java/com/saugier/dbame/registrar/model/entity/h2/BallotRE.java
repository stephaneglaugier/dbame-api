package com.saugier.dbame.registrar.model.entity.h2;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "ballots")
public class BallotRE implements Serializable {

    private static final String SEPARATOR = "||";

    @Id
    private long id;

    @Column
    private Date timestamp;

    @Column
    private int randint;

    @Column
    private String w;

    @Column
    private String s;

    @Column
    private long permutation;

    public static String getSEPARATOR() {
        return SEPARATOR;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public int getRandint() {
        return randint;
    }

    public void setRandint(int randint) {
        this.randint = randint;
    }

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
