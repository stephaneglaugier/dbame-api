package com.saugier.dbame.registrar.exception;

import com.saugier.dbame.core.model.base.Person;

public class AlreadyRegisteredException extends Exception{

    Person person;

    public AlreadyRegisteredException(String msg, Person person){

        super(msg);
        this.person = person;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
