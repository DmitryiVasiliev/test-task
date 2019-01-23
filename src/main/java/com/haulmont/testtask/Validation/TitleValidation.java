package com.haulmont.testtask.Validation;

import com.vaadin.data.validator.AbstractStringValidator;
import com.vaadin.ui.Table;

import java.util.Collection;

public class TitleValidation extends AbstractStringValidator {
    /**
     * Constructs a validator for strings.
     *
     * <p>
     * Null and empty string values are always accepted. To reject empty values,
     * set the field being validated as required.
     * </p>
     *
     * @param errorMessage the message to be included in an {@link InvalidValueException}
     * (with "{0}" replaced by the value that failed validation).
     */
    private Table table;

    public TitleValidation(Table table, String errorMessage) {
        super(errorMessage);
        this.table = table;
    }

    @Override
    protected boolean isValidValue(String value) {
        if (value.length() < 1) return false;
        Collection collection = table.getItemIds();
        for (int i = 1; i < collection.size(); i++) {
            if ((table.getContainerDataSource().getItem(i).toString().split(" ")[1].toLowerCase().trim()).equals(value.toLowerCase().trim()))
                return false;
        }
        return true;
    }
}
