package com.saugier.dbame.core.model.base;

import java.math.BigInteger;

/**
 * Class wrapping data allowing for simplified conversion between String, byte[] and BigInteger data types.
 */
public class Datum {

    private static int DEFAULT_RADIX = 16;

    private BigInteger data;

    public Datum(){};

    public Datum(byte[] data){
        this.data = new BigInteger(data);
    }

    public Datum(String data){
        this.data = new BigInteger(data, DEFAULT_RADIX);
    }

    public Datum(BigInteger data){
        this.data = data;
    }

    public boolean equals(Datum d){
        return this.data.equals(d.data);
    }

    public byte[] toBytes() {
        return data.toByteArray();
    }

    @Override
    public String toString() {
        return this.data.toString(DEFAULT_RADIX);
    }

    public BigInteger toBigInt() {
        return this.data;
    }

}
