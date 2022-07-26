package com.saugier.dbame.moderator.model.entity;

import com.saugier.dbame.core.model.entity.Roll;

import javax.persistence.*;

@Entity
@Table(name = "encrypted_ballots")
public class EncryptedBallot {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="roll_id")
    private Roll roll;

    @Column
    private String cypherText;

    @Column
    private String blindFactor;

    @Column
    private String ephemeralKey;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Roll getRoll() {
        return roll;
    }

    public void setRoll(Roll roll) {
        this.roll = roll;
    }

    public String getCypherText() {
        return cypherText;
    }

    public void setCypherText(String cypherText) {
        this.cypherText = cypherText;
    }

    public String getBlindFactor() {
        return blindFactor;
    }

    public void setBlindFactor(String blindFactor) {
        this.blindFactor = blindFactor;
    }

    public String getEphemeralKey() {
        return ephemeralKey;
    }

    public void setEphemeralKey(String ephemeralKey) {
        this.ephemeralKey = ephemeralKey;
    }
}
