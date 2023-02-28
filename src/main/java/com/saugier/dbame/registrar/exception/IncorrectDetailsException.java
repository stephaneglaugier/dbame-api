package com.saugier.dbame.registrar.exception;

import com.saugier.dbame.registrar.model.entity.mysql.PersonRE;

public class IncorrectDetailsException extends Exception{

    private PersonRE personRE;

    public IncorrectDetailsException(String msg, PersonRE personRE){
        super(msg);
        this.personRE = personRE;
    }

    public PersonRE getPerson() {
        return personRE;
    }

    public void setPerson(PersonRE personRE) {
        this.personRE = personRE;
    }
}
