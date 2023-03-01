package com.saugier.dbame.registrar.model.entity.h2;

import javax.persistence.*;

@Entity
@Table(name="permutation_moderator")
public class PermutationME {

    @Id
    private long id;

    @Column(name = "permutation")
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
