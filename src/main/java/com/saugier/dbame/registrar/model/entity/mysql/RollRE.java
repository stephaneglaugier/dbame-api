package com.saugier.dbame.registrar.model.entity.mysql;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "roll")
public class RollRE implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String y;

    @Column(name = "w")
    private String w;

    @Column(name = "s")
    private String s;

    
    public boolean equals(Object o){
        if (o == this) return true;
        if (!(o instanceof RollRE)) return false;
        RollRE r = (RollRE) o;
        if (id!=r.getId()) return false;
        if (!y.equalsIgnoreCase(r.getY())) return false;
        if (!w.equalsIgnoreCase(r.getW())) return false;
        if (!s.equals(r.getS())) return false;
        return true;

    }

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
