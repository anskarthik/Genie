package com.coacle.genie.api;

import com.coacle.genie.model.ValidationErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@ControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ValidationErrorDto processValidationError(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        List<ObjectError> objectErrors = result.getGlobalErrors();

        ValidationErrorDto dto = new ValidationErrorDto();

        for (FieldError fieldError : fieldErrors) {
            String localizedErrorMessage = fieldError.getDefaultMessage();
            dto.addFieldError(fieldError.getField(), localizedErrorMessage);
        }

        for (ObjectError objectError : objectErrors) {
            String localizedErrorMessage = objectError.getDefaultMessage();
            dto.addObjectError(localizedErrorMessage);
        }

        return dto;
    }

}
