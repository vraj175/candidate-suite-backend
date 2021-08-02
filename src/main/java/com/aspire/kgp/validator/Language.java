package com.aspire.kgp.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LanguageValidator.class)
@Documented
public @interface Language {
  String message() default "Enter valid Language.";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}