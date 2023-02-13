package com.saugier.dbame.core.model.base;

import java.math.BigInteger;

/**
 * Masked ballot request created and sent by the Moderator to the Registrar
 */
public class MaskedRequest {

    private BigInteger maskedData;
    private long permutation;

    public MaskedRequest(BigInteger maskedData, long permutation){
        this.maskedData = maskedData;
        this.permutation = permutation;
    }

    public BigInteger getMaskedData() {
        return maskedData;
    }

    public void setMaskedData(BigInteger maskedData) {
        this.maskedData = maskedData;
    }

    public long getPermutation() {
        return permutation;
    }

    public void setPermutation(long permutation) {
        this.permutation = permutation;
    }
}
