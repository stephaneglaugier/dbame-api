package com.saugier.dbame.registrar.exception;

public class PublicKeyNotFoundException extends Exception{

    private String publicKey;

    public PublicKeyNotFoundException(String s, String publicKey) {
        super(s);
        this.publicKey = publicKey;
    }
}
