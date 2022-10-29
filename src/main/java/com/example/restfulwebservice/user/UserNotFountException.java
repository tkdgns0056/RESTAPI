package com.example.restfulwebservice.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// HTTP Status code
// 응답코드가 2XX -> OK
// 응답코드가 4XX -> Client 문제
// 응답코드가 5XX -> Server 문제
@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFountException extends RuntimeException {
    public UserNotFountException(String message) {
        super(message);
    }
}
