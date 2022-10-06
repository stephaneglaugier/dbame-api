package com.saugier.dbame.core.model.base;

import java.math.BigInteger;

/**
 * Class representing an entry into the electoral roll.
 */
public class Roll {

    private Datum publicKey;
    private Signature signature;

    public Roll(){
        this.publicKey = new Datum();
        this.signature = new Signature();
    }

    public Roll(Datum publicKey, Signature signature){
        this();
        this.setPublicKey(publicKey);
        this.setSignature(signature);
    }

    public Roll(String publicKey, Signature signature){
        this(new Datum(publicKey), signature);
    }

    public Roll(BigInteger publicKey, Signature signature){
        this(new Datum(publicKey), signature);
    }

    public Roll(byte[] publicKey, Signature signature){
        this(new Datum(publicKey), signature);
    }

    public boolean equals(Roll nr){
        if (!publicKey.equals(nr.getPublicKey())) return false;
        if (!signature.equals(nr.getSignature())) return false;
        return true;
    }

    public Datum getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(Datum publicKey) {
        this.publicKey = publicKey;
    }

    public Signature getSignature() {
        return signature;
    }

    public void setSignature(Signature signature) {
        this.signature = signature;
    }
}
