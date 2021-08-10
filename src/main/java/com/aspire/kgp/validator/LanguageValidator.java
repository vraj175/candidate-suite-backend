package com.aspire.kgp.validator;

import java.util.Arrays;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.aspire.kgp.constant.Constant;

public class LanguageValidator implements ConstraintValidator<Language, String> {
  
  List<String> languages = Arrays.asList(Constant.ENGLISH, Constant.SPANISH, Constant.PORTUGUESE);

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return languages.contains(value);
  }

}
