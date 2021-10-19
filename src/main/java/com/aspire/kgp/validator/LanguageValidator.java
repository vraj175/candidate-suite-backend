package com.aspire.kgp.validator;

import java.util.Arrays;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.aspire.kgp.constant.Constant;

public class LanguageValidator implements ConstraintValidator<Language, String> {

  List<String> languages =
      Arrays.asList(Constant.ENGLISH_CODE, Constant.SPANISH_CODE, Constant.PORTUGUESE_CODE);

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return languages.contains(value);
  }

}
