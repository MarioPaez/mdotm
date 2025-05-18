package com.mdotm.petmanager.controller;

import com.mdotm.petmanager.exception.PetNotFoundException;
import com.mdotm.petmanager.exception.SortNotAllowedException;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void handleValidationErrors_returnsFieldErrors() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        when(ex.getBindingResult()).thenReturn(bindingResult);

        FieldError error1 = new FieldError("objectName", "field1", "must not be null");
        FieldError error2 = new FieldError("objectName", "field2", "size must be >= than 0");
        when(bindingResult.getFieldErrors()).thenReturn(List.of(error1, error2));

        ResponseEntity<Map<String, String>> response = exceptionHandler.handleValidationErrors(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).containsEntry("field1", "must not be null");
        assertThat(response.getBody()).containsEntry("field2", "size must be >= than 0");
    }

    @Test
    void handleConstraintViolation_returnsBadRequestWithMessage() {
        ConstraintViolationException ex = new ConstraintViolationException("Invalid value", null);

        ResponseEntity<String> response = exceptionHandler.handleConstraintViolation(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).startsWith("Validation error:");
    }

    @Test
    void handlePetNotFound_returnsNotFoundWithMessage() {
        PetNotFoundException ex = new PetNotFoundException("Pet not found");

        ResponseEntity<String> response = exceptionHandler.handlePetNotFound(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo("Pet not found");
    }

    @Test
    void handleSortNotAllowed_returnsBadRequestWithMessage() {
        SortNotAllowedException ex = new SortNotAllowedException("Sort by age not allowed");

        ResponseEntity<String> response = exceptionHandler.handleSortNotAllowed(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).startsWith("Sort error:");
    }

    @Test
    void handleHttpMessageConversionException_returnsBadRequestWithFixedMessage() {
        HttpMessageConversionException ex = new HttpMessageConversionException("Conversion failed");

        ResponseEntity<String> response = exceptionHandler.handleHttpMessageConversionException(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo("Specie not valid. Must to be one of the next ones: [RABBIT, TURTLE, CAT, DOG]");
    }
}
