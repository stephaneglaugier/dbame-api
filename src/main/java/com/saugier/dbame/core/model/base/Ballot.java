package com.saugier.dbame.core.model.base;


import java.util.Date;

/**
 * Class representing a d-BAME signed, un-encrypted ballot
 *
 * Generated by Registrar prior to its encryption following a ballot request.
 */
public class Ballot {

    private long id;
    private Date timestamp;
    private int randint;
    private Signature signature;

    /**
     * Returns a 256-bit representation of the Ballot's attributes as a 32-byte String with padded zeros.
     * @return 0x{id||timestamp||randint}
     */
    public String asMessage(){

        String _id = pad(Long.toHexString(id), 8);
        String _timestamp = pad(Long.toHexString(timestamp.toInstant().getEpochSecond()), 8);
        String _randint = pad(Integer.toHexString(randint), 8);
        String _zeros = "00000000";


        String s =  new String()
                .concat(_id)
                .concat(_timestamp)
                .concat(_randint)
                .concat(_zeros);
        if (s.length() != 32) throw new RuntimeException(String.format("Ballot is not 256 bits: %s"));
        return s;
    }

    /**
     * Pads a String with zeros up to a given length
     *
     * @exception IllegalArgumentException thrown if in.length() is greater than length parameter
     *
     * @param in
     * @param length
     * @return
     */
    private String pad(String in, int length){

        if (in.length() > length)
            throw new IllegalArgumentException("string cannot be longer than length parameter");

        String out = in;
        while (out.length()<length){
            out = "0" + out;
        }
        return out;
    }



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public int getRandint() {
        return randint;
    }

    public void setRandint(int randint) {
        this.randint = randint;
    }

    public Signature getSignature() {
        return signature;
    }

    public void setSignature(Signature signature) {
        this.signature = signature;
    }
}
