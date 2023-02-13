package com.saugier.dbame.core.model.base;

import java.math.BigInteger;

/**
 * Class representing an El-Gamal Signature
 */
public final class Signature {

    private BigInteger w;
    private BigInteger s;

    public Signature(BigInteger w, BigInteger s){
        this.setW(w);
        this.setS(s);
    }

    public BigInteger getW() {
        return w;
    }

    public void setW(BigInteger w) {
        this.w = w;
    }

    public BigInteger getS() {
        return s;
    }

    public void setS(BigInteger s) {
        this.s = s;
    }
}
