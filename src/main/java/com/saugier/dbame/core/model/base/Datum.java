package com.saugier.dbame.core.model.base;

import java.math.BigInteger;

/**
 * Class wrapping data allowing for simplified conversion between String, byte[] and BigInteger data types.
 */
public class Datum {

    private static int DEFAULT_RADIX = 16;

    private String data;

    public Datum(){};

    public Datum(byte[] data){
        this.data = data.toString();
    }

    public Datum(String data){
        this.data = data;
    }

    public Datum(BigInteger data){
        this.data = data.toString(DEFAULT_RADIX);
    }

    public boolean equals(Datum d){
        return this.toString().equalsIgnoreCase(d.toString());
    }

    public byte[] toBytes() {
        return data.getBytes();
    }

    public void set(byte[] bytes) {
        this.data = bytes.toString();
    }

    @Override
    public String toString() {
        return this.data;
    }

    public void set(String string) {
        this.data = string;
    }

    public BigInteger toBigInt() {
        return new BigInteger(this.data, DEFAULT_RADIX);
    }

    public void set(BigInteger bigInteger) {
        this.data = bigInteger.toString(DEFAULT_RADIX);
    }
}
