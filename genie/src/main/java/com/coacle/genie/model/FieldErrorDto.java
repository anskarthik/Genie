package com.coacle.genie.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FieldErrorDto {

    private String field;

    private String message;

}
