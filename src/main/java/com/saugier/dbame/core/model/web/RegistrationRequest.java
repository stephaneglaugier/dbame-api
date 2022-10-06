package com.saugier.dbame.core.model.web;

import java.util.Date;

/**
 * Message passed from Voter to Registrar when registering to vote.
 */
public class RegistrationRequest {

    private long id;
    private String firstName;
    private String lastName;
    private Date dob;
    private String publicKey;
    private String s;
    private String w;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public String getW() {
        return w;
    }

    public void setW(String w) {
        this.w = w;
    }
}
