package com.riwi.hamilton.exception;

public class DuplicateResourceException
        extends DomainException {

    public DuplicateResourceException(
            String message
    ){
        super(message);
    }

}