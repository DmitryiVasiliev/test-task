package com.haulmont.testtask.Validation;

import com.vaadin.data.Validatable;
import com.vaadin.data.Validator;
import com.vaadin.data.validator.AbstractStringValidator;
import com.vaadin.data.validator.AbstractValidator;
import com.vaadin.ui.TextField;

import java.util.regex.Pattern;

public class StringValidation extends AbstractStringValidator {
    /**
     * Constructs a validator for strings.
     *
     * <p>
     * Null and empty string values are always accepted. To reject empty values,
     * set the field being validated as required.
     * </p>
     *
     * @param errorMessage the message to be included in an {@link InvalidValueException}
     *                     (with "{0}" replaced by the value that failed validation).
     */
    public StringValidation(String errorMessage) {
        super(errorMessage);

    }

    @Override
    protected boolean isValidValue(String value) {

        if (value.matches("(\\D|\\-)+"))
            return true;
        else return false;
    }
}
