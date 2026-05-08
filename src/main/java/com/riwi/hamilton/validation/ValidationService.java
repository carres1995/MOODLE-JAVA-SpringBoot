package com.riwi.hamilton.validation;

import java.util.List;

public class ValidationService <T>{

    public boolean idExist(Long id){
        if(id == null){
            throw new IllegalArgumentException("Id can´t be null");
        }
        return false;
    }
    public boolean ObjectExist(T object){
        if(object == null){
            throw new IllegalArgumentException("Object not added.");
        }
        return false;
    }
    public boolean ListValidation(List<T> lista) {
        if (lista == null || lista.isEmpty()) {
            throw new IllegalArgumentException("The list is empty or null.");
        }
        return true;
    }
}
