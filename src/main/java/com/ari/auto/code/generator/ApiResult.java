package com.ari.auto.code.generator;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.LinkedHashMap;

public class ApiResult<TResult> {
    private static final String SUCCESS = "SUCCESS";
    private static final String FAILURE = "FAILURE";

    public static <TResult> ResponseEntity<ResponseWrapper<TResult>> createSuccessResult(TResult result, HttpStatus status) {
        return ResponseEntity.status(status).body(ResponseWrapper.wrap(SUCCESS, result, status));
    }

    public static <TResult> ResponseEntity<ResponseWrapper<TResult>> createSuccessResult(TResult result, HttpStatus status, LinkedHashMap<String, Object> additionalInformation) {
        return ResponseEntity.status(status).body(ResponseWrapper.wrap(SUCCESS, result, status, additionalInformation));
    }

    public static <TResult> ResponseEntity<ResponseWrapper<TResult>> createFailureResult(TResult result, HttpStatus status) {
        return ResponseEntity.status(status).body(ResponseWrapper.wrap(FAILURE, result, status));
    }

    public static <TResult> ResponseEntity<ResponseWrapper<TResult>> createFailureResult(TResult result, HttpStatus status, LinkedHashMap<String, Object> additionalInformation) {
        return ResponseEntity.status(status).body(ResponseWrapper.wrap(FAILURE, result, status, additionalInformation));
    }
}
