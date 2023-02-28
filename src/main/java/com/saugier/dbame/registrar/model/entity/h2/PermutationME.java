package com.saugier.dbame.registrar.model.entity.h2;

import javax.persistence.*;

@Entity
@Table(name="permutation_moderator")
public class PermutationME {

    @Id
    private long id;

    @Column(name = "a")
    private long from;

    @Column(name = "b")
    private long to;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getFrom() {
        return from;
    }

    public void setFrom(long from) {
        this.from = from;
    }

    public long getTo() {
        return to;
    }

    public void setTo(long to) {
        this.to = to;
    }
}
