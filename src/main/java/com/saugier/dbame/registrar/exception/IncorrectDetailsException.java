package com.saugier.dbame.registrar.exception;

import com.saugier.dbame.core.model.entity.Person;

public class IncorrectDetailsException extends Exception{

    private Person person;

    public IncorrectDetailsException(String msg, Person person){
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
