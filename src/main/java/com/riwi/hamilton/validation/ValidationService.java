package com.riwi.hamilton.validation;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

@Component
public class ValidationService <T>{

    public void idExist(Long id){
        if(id == null){
            throw new IllegalArgumentException("Id can´t be null");
        }
    }
    public void ObjectExist(T object){
        if(object == null){
            throw new IllegalArgumentException("Object not added.");
        }
    }
    public void ListValidation(List<T> lista) {
        if (lista == null) {
            throw new IllegalArgumentException("The list is null.");
        }
    }
    public void uniqueId(Long id, List<T> list, Function<T,Long> extractId){
        boolean exist = list.stream().anyMatch(i -> extractId.apply(i).equals(id));
        if(exist){
            throw new IllegalArgumentException("Id already exists");
        }
    }
}
