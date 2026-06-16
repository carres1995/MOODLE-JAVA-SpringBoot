package com.riwi.hamilton.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EventValidator implements ConstraintValidator<ValidNameEvent, String> {
    @Override
    public  boolean isValid(String value, ConstraintValidatorContext context){
        if (value == null || value.isBlank()){
            return true;
        }

        boolean beginCorrect = value.startsWith("EVENT-");
        return beginCorrect;
    }
}
