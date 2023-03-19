package com.saugier.dbame.registrar.exception;

public class NotRegisteredException extends Exception {
    private long id;

    public NotRegisteredException(String message, long id) {
        super(message);
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}



