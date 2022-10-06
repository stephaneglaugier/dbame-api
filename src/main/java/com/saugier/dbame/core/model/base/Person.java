package com.saugier.dbame.core.model.base;

import java.util.Date;

/**
 * Class representing a Person on the electoral roll.
 */
public class Person {

    private long id;
    private String firstName;
    private String lastName;
    private Date dob;
    private Roll roll;

    public boolean equals(Person p){
        if (id != p.getId()) return false;
        if (!firstName.equalsIgnoreCase(p.getFirstName())) return false;
        if (!lastName.equalsIgnoreCase(p.getLastName())) return false;
        if (!dob.equals(p.getDob())) return false;
        if (!roll.equals(p.getRoll())) return false;
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

    public Roll getRoll() {
        return roll;
    }

    public void setRoll(Roll roll) {
        this.roll = roll;
    }
}
