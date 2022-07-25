package com.saugier.dbame.registrar.exception;

import com.saugier.dbame.core.model.entity.Roll;

public class AlreadyRegisteredException extends Exception{

    Roll roll;

    public AlreadyRegisteredException(String msg, Roll roll){

        super(msg);
        this.roll = roll;
    }

    public Roll getRoll() {
        return roll;
    }

    public void setRoll(Roll roll) {
        this.roll = roll;
    }
}
