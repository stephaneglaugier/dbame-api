package com.saugier.dbame.core.model.base;

import java.math.BigInteger;

/**
 * Class representing an entry into the electoral roll.
 */
public class Roll {

    private BigInteger publicKey;
    private Signature signature;

    public Roll(BigInteger publicKey, Signature signature){
        this.setPublicKey(publicKey);
        this.setSignature(signature);
    }

    public BigInteger getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(BigInteger publicKey) {
        this.publicKey = publicKey;
    }

    public Signature getSignature() {
        return signature;
    }

    public void setSignature(Signature signature) {
        this.signature = signature;
    }
}
