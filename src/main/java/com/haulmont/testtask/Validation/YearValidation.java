package com.haulmont.testtask.Validation;

import com.vaadin.data.validator.AbstractStringValidator;

public class YearValidation extends AbstractStringValidator {
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
    public YearValidation(String errorMessage) {
        super(errorMessage);
    }

    @Override
    protected boolean isValidValue(String value) {
        try {
            int year = Integer.parseInt(value);
            if (year > 1000 && year < 2020) return true;
            else return false;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
