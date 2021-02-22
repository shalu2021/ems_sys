package com.employee.exception;

import com.employee.enums.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@ControllerAdvice
@Slf4j
public class RestExceptionHandler {

    @Getter
    private Map<String, ErrorCode> errorCodeMapper = new HashMap<>();

    public RestExceptionHandler() {

        errorCodeMapper.put("could not execute statement; SQL [n/a]; constraint [\"PUBLIC.UK_HAFQWJQE2E9BCPGYJ6EVM52AP_INDEX_7 ON PUBLIC.EMPLOYEE(NAME) VALUES 1\"; SQL statement:", ErrorCode.NAME_ALREADY_EXISTS);

    }

    private ResponseEntity<Object> doHandleException(ErrorCode errorCode) {

        HttpStatus httpStatus = null;
        try {
            httpStatus = HttpStatus.valueOf(errorCode.getData().getHttpResponseCode());
        } catch (IllegalArgumentException illegalArgumentException) {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        ErrorCode.Data data = errorCode.getData();
        data.setLabel(errorCode.toString());

        ObjectMapper objectMapper = new ObjectMapper();
        String errorCodeJson = String.format("{ \"errorCode\": %s}", errorCode.getData().getCode());
        try {
            errorCodeJson = objectMapper.writeValueAsString(errorCode.getData());
        } catch (IOException i) {
            // pass
        }

        return ResponseEntity //
                .status(httpStatus)
                .body(errorCodeJson);
    }


    @ExceptionHandler(DataIntegrityViolationException.class)
    public @ResponseBody
    ResponseEntity<Object> handleException(DataIntegrityViolationException e) {
        System.out.println("getErrorCodeMapper():"+getErrorCodeMapper().toString());
        System.out.println("e.getMessage():"+e.getMessage());
        Optional<String> matchingException =
                getErrorCodeMapper().keySet().stream().filter(m -> e.getMessage().contains(m)).findAny();

        if (matchingException.isEmpty()) {
            // No match, and in this case, we want to see it!
            //
            System.out.println("matching: "+matchingException.get());
            throw new RuntimeException(e);
        }

        return doHandleException(getErrorCodeMapper().get(matchingException.get()));
    }

    @ExceptionHandler(UnexpectedRollbackException.class)
    public @ResponseBody
    ResponseEntity<Object> handleException(UnexpectedRollbackException e) {

        String message = e.getCause().getCause().getCause().getCause().getMessage();

        Optional<String> matchingException =
                getErrorCodeMapper().keySet().stream().filter(m -> message.contains(m)).findAny();

        if (matchingException.isEmpty()) {
            throw new RuntimeException(e);
        }

        return doHandleException(getErrorCodeMapper().get(matchingException.get()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    ResponseEntity<Object> handleException(ConstraintViolationException ex) {

        Map<String, List<String>> errors = ex.getConstraintViolations().stream()
                .collect(groupingBy(
                        constraintViolation -> constraintViolation.getPropertyPath().toString(),
                        Collectors.mapping(ConstraintViolation::getMessage, Collectors.toList())
                ));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

}
