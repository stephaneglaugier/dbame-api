package com.saugier.dbame.registrar.model.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "voters")
public class Person {

    @Id
    private long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "dob")
    @Temporal(TemporalType.DATE)
    private Date dob;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="key_id")
    private Key key;

    @Override
    public boolean equals(Object o){
        if (o == this) return true;
        if (!(o instanceof Person)) return false;
        Person p = (Person) o;
        if (id!=p.getId()) return false;
        if (!firstName.equalsIgnoreCase(p.getFirstName())) return false;
        if (!lastName.equalsIgnoreCase(p.getLastName())) return false;
        if (!dob.equals(p.getDob())) return false;
        return true;

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        this.key = key;
    }
}
