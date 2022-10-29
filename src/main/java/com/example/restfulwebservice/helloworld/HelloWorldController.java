package com.example.restfulwebservice.helloworld;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@RestController
public class HelloWorldController {

    @Autowired //현재 스프링 프레임워크에 등록되어 있는 빈중에서 같은 타입으로 등록되어 있는 빈을 자동으로 주입 시켜 준다.
    private MessageSource messageSource;

    //GET
    @GetMapping("/hello-world")
    public String helloWorld(){
        return "Hello World";
    }

    //자바 빈 형태로 API 호출
    @GetMapping("/hello-world-bean")
    public HelloWorldBean helloWorldBean(){

        return new HelloWorldBean("Hello World");
    }

    //PathVariable 이용해서 호출
    //@PathVariable 이용해서 가변 변수 사용(String name)
    //파라미터와 다른 값을 넣어 주려면 @PathVariable(value="") 지정해주면됨.
    @GetMapping("/hello-world-bean/path-variable/{name}")
    public HelloWorldBean helloWorldBean(@PathVariable String name){

        return new HelloWorldBean(String.format("Hello World," + name));
    }

    //다국어 처리를 위한 컨트롤러
    @GetMapping(path="/hello-world-internationalized")
    public String helloWorldInternationalized(@RequestHeader(name="Accept-Language", required = false) Locale locale){

        return messageSource.getMessage("greeting.message",null, locale);
    }
}
