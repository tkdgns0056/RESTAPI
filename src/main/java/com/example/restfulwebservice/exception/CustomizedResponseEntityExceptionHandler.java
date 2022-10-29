package com.example.restfulwebservice.exception;

import com.example.restfulwebservice.user.UserNotFountException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

/**
 * 공통적으로 예외 처리를 해주는 클래스 즉, 스프링  AOP 이다.
 *
 * ResponseEntityExceptionHandler : 클라이언트 요청에 대한 처리 과정에서 발생하는 오류를 적절하게 제어하기 위해서 상속 받음.
 * ExceptionHandler 클래스로 등록한 예외가 발생하면, 해당 예외를 처리하기 위한 메소드와 처리 오류 등을 반환하기 위한 객체로 사용
 */
@RestController
@ControllerAdvice // <- 모든 컨트롤러가 실행이 될 때 반드시 이 어노테이션을 가지고 있는 빈이 사전에 실행 된다. AOP
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllException(Exception ex, WebRequest request){
        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));

        return new ResponseEntity(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * NOT_FOUNT ERROR 관련된 메소드
     */
    @ExceptionHandler(UserNotFountException.class)
    public final ResponseEntity<Object> handleUserNotFountException(Exception ex, WebRequest request){
        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));

        return new ResponseEntity(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), "Validation Failed", ex.getBindingResult().toString());

        return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
    }
}
