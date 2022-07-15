package com.saugier.dbame.registrar.model.entity;

import javax.persistence.*;
import java.math.BigInteger;

@Entity
@Table(name = "keys")
public class Key {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;

    @Column(name = "bytes")
    private String bytes;

//    @OneToOne(mappedBy = "key")
//    private Person person;

    public BigInteger asBI(){
        return new BigInteger(bytes);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBytes() {
        return bytes;
    }

    public void setBytes(String bytes) {
        this.bytes = bytes;
    }

//    public Person getPerson() {
//        return person;
//    }
//
//    public void setPerson(Person person) {
//        this.person = person;
//    }
}
