package com.saugier.dbame.registrar.exception;

import com.saugier.dbame.registrar.model.entity.Roll;

public class AlreadyRegisteredException extends Exception{

    Roll roll;

    public AlreadyRegisteredException(String message, Roll roll){

        super(message);
        this.roll = roll;
    }

    public Roll getRoll() {
        return roll;
    }

    public void setRoll(Roll roll) {
        this.roll = roll;
    }
}
