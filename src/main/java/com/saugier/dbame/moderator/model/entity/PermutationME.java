package com.saugier.dbame.moderator.model.entity;

import javax.persistence.*;

@Entity
public class PermutationME {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private long permutation;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPermutation() {
        return permutation;
    }

    public void setPermutation(long permutation) {
        this.permutation = permutation;
    }
}
