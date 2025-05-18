package com.mdotm.petmanager.exception;

public class SortNotAllowedException extends RuntimeException{

    public SortNotAllowedException(String message){
        super(message);
    }
}
