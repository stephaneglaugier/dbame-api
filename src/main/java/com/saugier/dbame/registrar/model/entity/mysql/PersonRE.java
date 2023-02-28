package com.saugier.dbame.registrar.model.entity.mysql;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "voters")
public class PersonRE {

    @Id
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "date_of_birth")
    @Temporal(TemporalType.DATE)
    private Date dob;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="roll_id")
    private RollRE rollRE;

    
    public boolean equals(Object o){
        if (o == this) return true;
        if (!(o instanceof PersonRE)) return false;
        PersonRE p = (PersonRE) o;
        if (id!=p.getId()) return false;
        if (!firstName.equalsIgnoreCase(p.getFirstName())) return false;
        if (!lastName.equalsIgnoreCase(p.getLastName())) return false;
        if (!dob.equals(p.getDob())) return false;
        return true;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public RollRE getRoll() {
        return rollRE;
    }

    public void setRoll(RollRE rollRE) {
        this.rollRE = rollRE;
    }
}
