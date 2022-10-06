package com.saugier.dbame.core.model.base;

import java.math.BigInteger;

/**
 * Class representing an El-Gamal Signature
 */
public final class Signature {

    private Datum w;
    private Datum s;

    public Signature(){
        this.w = new Datum();
        this.s = new Datum();
    }

    public Signature(Datum w, Datum s){
        this();
        this.setW(w);
        this.setS(s);
    }

    public Signature(String w, String s){
        this(new Datum(w), new Datum(s));
    }

    public Signature(BigInteger w, BigInteger s){
        this(new Datum(w), new Datum(s));
    }

    public Signature(byte[] w, byte[] s){
        this(new Datum(w), new Datum(s));
    }

    public boolean equals(Signature sig) {
        if (!w.equals(sig.getW())) return false;
        if (!s.equals(sig.getS())) return false;
        return true;
    }

    public Datum getW() {
        return w;
    }

    public void setW(Datum w) {
        this.w = w;
    }

    public Datum getS() {
        return s;
    }

    public void setS(Datum s) {
        this.s = s;
    }
}
