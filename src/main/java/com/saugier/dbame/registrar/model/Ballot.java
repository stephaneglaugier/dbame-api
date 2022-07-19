package com.saugier.dbame.registrar.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@MappedSuperclass
public class Ballot implements Serializable {

    private static final String SEPARATOR = "||";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private Date timestamp;

    @Column
    private int randint;

    public String to256Hex(){
        String out = this.toString();
        if (out.length() > 32) throw new RuntimeException("Ballot is greater than 256 bits.");
        while (out.length() < 32){
            out = "0" + out;
        }
        return out;
    }

    @Override
    public String toString(){
        String out =  new String()
                .concat(Long.toHexString(id))//.concat(SEPARATOR)
                .concat(Long.toHexString(timestamp.toInstant().getEpochSecond()))//.concat(SEPARATOR)
                .concat(Integer.toHexString(randint));
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
}
