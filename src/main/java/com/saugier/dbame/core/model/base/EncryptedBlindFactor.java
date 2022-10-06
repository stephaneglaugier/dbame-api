package com.saugier.dbame.core.model.base;

/**
 * Class representing a d-BAME encrypted blind factor.
 *
 * Sent by the Registrar to the Moderator for eventual use by the Voter.
 */
public class EncryptedBlindFactor {

    private Datum c1;
    private Datum c2;

    public Datum getC1() {
        return c1;
    }

    public void setC1(Datum c1) {
        this.c1 = c1;
    }

    public Datum getC2() {
        return c2;
    }

    public void setC2(Datum c2) {
        this.c2 = c2;
    }
}
