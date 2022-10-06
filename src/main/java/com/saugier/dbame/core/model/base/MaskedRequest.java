package com.saugier.dbame.core.model.base;

/**
 * Masked ballot request created and sent by the Moderator to the Registrar
 */
public class MaskedRequest {

    private Datum maskedData;
    private long permutation;


    public MaskedRequest(){
        this.maskedData = new Datum();
    }

    public MaskedRequest(Datum maskedData, long permutation){
        this.maskedData = maskedData;
        this.permutation = permutation;
    }

    public Datum getMaskedData() {
        return maskedData;
    }

    public void setMaskedData(Datum maskedData) {
        this.maskedData = maskedData;
    }

    public long getPermutation() {
        return permutation;
    }

    public void setPermutation(long permutation) {
        this.permutation = permutation;
    }
}
