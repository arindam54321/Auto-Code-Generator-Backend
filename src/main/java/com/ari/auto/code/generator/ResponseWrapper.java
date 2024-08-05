package com.ari.auto.code.generator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.LinkedHashMap;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseWrapper<TResult> {
    String type;
    HttpStatus status;
    TResult result;
    LinkedHashMap<String, Object> additionalInformation;

    public static <TResult> ResponseWrapper<TResult> wrap(String type, TResult result, HttpStatus status) {
        return new ResponseWrapper<TResult>(type, status, result, null);
    }

    public static <TResult> ResponseWrapper<TResult> wrap(String type, TResult result, HttpStatus status, LinkedHashMap<String, Object> additionalInformation) {
        return new ResponseWrapper<TResult>(type, status, result, additionalInformation);
    }
}
