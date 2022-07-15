package com.saugier.dbame.registrar.model.entity;

import javax.persistence.*;

@Entity
public class Roll {

    @Id@GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="key_id")
    private Key key;

    @Column(name = "w")
    private String w;

    @Column(name = "s")
    private String s;

    public Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        this.key = key;
    }

    public String getW() {
        return w;
    }

    public void setW(String w) {
        this.w = w;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }
}
