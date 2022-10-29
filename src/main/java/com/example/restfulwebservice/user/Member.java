package com.example.restfulwebservice.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
//@JsonIgnoreProperties(value={"password"})
//@JsonFilter("UserInfo")
@ApiModel(description = "사용자 상세 정보를 위한 도메인 객체")
@Entity
public class Member {

    @Id
    @GeneratedValue //시퀀스 자동으로
    private Integer id;

    @Size(min=2, message = "Name은 2글자 이상 입력해 주세요.")
    @ApiModelProperty(notes="사용자 이름을 입력해 주세요.")
    private String name;

    @Past
    @ApiModelProperty(notes="사용자 등록일을 입력해 주세요.")
    private Date join_date;

    @ApiModelProperty(notes="사용자 패스워드를 입력해 주세요.")
    private String password;

    @ApiModelProperty(notes="사용자 주민번호를 입력해 주세요.")
    private String ssn;

    @OneToMany(mappedBy = "member") //member 테이블과 매핑 시켜준다.
    private List<Post> posts;

    public Member(int id, String name, Date join_date, String password, String ssn){
        this.id = id;
        this.name = name;
        this.join_date = join_date;
        this.password = password;
        this.ssn = ssn;
    }

}
