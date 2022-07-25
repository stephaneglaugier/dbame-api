package com.saugier.dbame.core.model.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Entity
public class Roll implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String y;

    @Column(name = "w")
    private String w;

    @Column(name = "s")
    private String s;

    public Map<String, String> getYSW() {
        HashMap<String, String> out = new HashMap();
        out.put("y", getY());
        out.put("s", getS());
        out.put("w", getW());
        return out;
    }

    public void setYSW(Map<String, String> in) {
        this.setY(in.get("y"));
        this.setS(in.get("s"));
        this.setW(in.get("w"));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
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
