package com.example.restfulwebservice.helloworld;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor // 매개변수 없는 디폴트 생성자 만들어 주는 어노테이션
public class HelloWorldBean {

    private String message;
}
