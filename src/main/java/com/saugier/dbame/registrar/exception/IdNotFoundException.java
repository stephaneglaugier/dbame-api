package com.saugier.dbame.registrar.exception;

public class IdNotFoundException extends Exception{

    private long id;

    public IdNotFoundException(String msg, long id){
        super(msg);
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
