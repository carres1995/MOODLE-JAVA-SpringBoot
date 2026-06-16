package com.riwi.hamilton.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EventValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidNameEvent {
    String message() default "Formato de nombre invalido, debe empezar con (EVENT-)";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
