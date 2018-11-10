package com.coacle.genie.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ValidationErrorDto {

    private List<FieldErrorDto> fieldErrors = new ArrayList<>();
    private List<String> objectErrors = new ArrayList<>();

    public void addFieldError(String field, String message) {
        FieldErrorDto error = new FieldErrorDto(field, message);
        fieldErrors.add(error);
    }

    public void addObjectError(String message) {
        objectErrors.add(message);
    }

}