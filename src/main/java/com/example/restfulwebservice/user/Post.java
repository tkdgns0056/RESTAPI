package com.example.restfulwebservice.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue
    private Integer id;

    private String description;

    //Member : Post -> 1: N (0~N)
    // Main -> Member ,  Sub -> Post
    // FetchType.LAZY -> 지연로딩, Post Entity가 계속 로딩되는것이 아닌, 필요한 것만 로딩되게
    @ManyToOne(fetch = FetchType.LAZY )
    @JsonIgnore //외부에 데이터를 노출 시키지 않기  위해서 추가
    private Member member;

}
