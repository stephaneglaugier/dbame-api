package com.saugier.dbame.moderator.model.entity;

import javax.persistence.*;

@Entity
@Table(name = "encrypted_ballots")
public class ModeratorRelayME {

    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column
    private String y;

    @Column
    private String maskedY;

    @Column
    private String w;

    @Column
    private String s;

    @Column
    private long permutation;

    @Column
    private String blindFactor;

    @Column
    private String cypherText;

    @Column
    private String ephemeralKey;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public String getMaskedY() {
        return maskedY;
    }

    public void setMaskedY(String maskedY) {
        this.maskedY = maskedY;
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

    public String getBlindFactor() {
        return blindFactor;
    }

    public void setBlindFactor(String blindFactor) {
        this.blindFactor = blindFactor;
    }

    public String getCypherText() {
        return cypherText;
    }

    public void setCypherText(String cypherText) {
        this.cypherText = cypherText;
    }

    public String getEphemeralKey() {
        return ephemeralKey;
    }

    public void setEphemeralKey(String ephemeralKey) {
        this.ephemeralKey = ephemeralKey;
    }
}
