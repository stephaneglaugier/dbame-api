package com.saugier.dbame.core.model.web;

/**
 * Response passed from the Registrar to the Voter after having registered to vote.
 */
public class RegistrationResponse extends RegistrationRequest{

    private String s;
    private String w;

    @Override
    public String getS() {
        return s;
    }

    @Override
    public void setS(String s) {
        this.s = s;
    }

    @Override
    public String getW() {
        return w;
    }

    @Override
    public void setW(String w) {
        this.w = w;
    }
}
