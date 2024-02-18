package com.krososs.trains.rest.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.validation.ObjectError;

public class Response {

    public static Map<String, List<String>> getErrorResponse(List<ObjectError> errors){
        Map<String, List<String>> result = new HashMap<>();
        List<String> resultErrors = new ArrayList<>();

        errors.forEach(err -> resultErrors.add(err.getDefaultMessage()));
        result.put("Errors", resultErrors);
        return  result;
    }
}