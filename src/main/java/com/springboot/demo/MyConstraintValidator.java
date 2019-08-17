package com.springboot.demo;

import com.springboot.demo.annotation.MyConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MyConstraintValidator implements ConstraintValidator<MyConstraint, String> {

    private String name;

    @Override
    public void initialize(MyConstraint myConstraint) {
        name = myConstraint.name();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (name.equals(value)){
            return true;
        }
        return false;
    }
}
