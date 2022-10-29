package com.example.restfulwebservice.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor //모든 필드를 가지고 있는 생성자 만들어 주는 생성자
@NoArgsConstructor //기본 생성자 만들어 주는 어노테이션
public class ExceptionResponse {
    private Date timestamp;
    private String message;
    private String details;
}
