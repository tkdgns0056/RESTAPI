package com.example.restfulwebservice.user;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class UserController {

    private UserDaoService service;

     /*
         생성자를 이용한 의존성 주입
         생성자가 하나인 경우에는 @Autowired를 붙이지 않아도 되지만,
         2개 이상인 경우에는 어노테이션을 붙여주어야 한다.
         생성자를 통해 주입하게 되면 객체를 생성하지 시점에 필요한 인스턴스를 주입하며,
         순환 참조를 방지 할 수 있다.
     */
     public UserController(UserDaoService service){

         this.service = service;
    }

    @GetMapping("/users")
    public List<Member> retrieveAllUsers(){

         return service.findAll();
    }

    // GET /users/1 or /users/10.. -> 서버측에는 String(문자형태로 반환됨)
    // @GetMapping("/users/{id}")
    //문자 형태로 넘어오지만 int 형태로 자동 매핑됨. 그래서 편리하다.
    public Member retrieveUser(@PathVariable int id){
        Member user = service.findOne(id);

        /*
          예를 들어서 유저의 ID값이 100번이 없는데 입력 후 출력을 하면 상태코드가 200이 나온다.
          이 상태코드는 올바르지 않은 상태 코드이다. 이런 부분을 방지하기 위해서 예외 처리를 통해
          상태 코드를 지정 시켜줘야 한다. Client에서 입력을 잘못 한 경우에는 400에러로 갈 수 있도록
          처리를 해주는 것이 좋다.(ex. 404 -> Not Fount)
         */
        if(user == null){
            throw new UserNotFountException(String.format("ID[%s] not fount", id));
        }

        return user;
    }

    /**
     * HATEOAS 이용한 컨트롤러 로직
     *
     * 우리는 id를 숫자로 해도 서버측에 전달 될 경우에는 -> String으로 된다
     *  id로 하면 자동으로 원하는 int에 맞게 찾아준다
     *  HETAOS를 적용하면 개발자의 양은 많아지지만
     *  내가 개발한 것을 보는 사용자입장에서는 더 많은 정보를 알 수 있다
     *
     *  사용자 상세 정보
     * @param id
     * @return
     */
    @GetMapping("/users/{id}")
    //문자 형태로 넘어오지만 int 형태로 자동 매핑됨. 그래서 편리하다.
    public EntityModel<Member> retrieveUserHATEOAS(@PathVariable int id){
        Member user = service.findOne(id);

        if(user == null){
            throw new UserNotFountException(String.format("ID[%s] not fount", id));
        }

        // HATEOAS 관련 코드
        //"all-users", SERVER_PATH + "/users"
        //retrieveAllUsers
        EntityModel<Member> model = EntityModel.of(user);
        WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).retrieveAllUsers());
        model.add(linkTo.withRel("all-users"));

        return model;
    }

    //단일 등록 이지만 restful API를 설계 할 때 복수형 설계 권장한다.
    //users라는 데이터 리소스에 새로운 목적의 데이터를 추가하기 위한 API이기 때문.
    @PostMapping("/users")

    /*
       ResponseEntity 사용 하게 되면 반환 데이터를
       Response Status Code를 포함해서 전달 할 수 있기 때문에, 단순하게 500에러나, 200 성공 코드
       이외의 다른 상태 값들도 전달 할 수 있기 때문이다.

       또한 createUser 메서드에서 ResponseEntity 사용한 목적은
       HATEOS 기능을 사용해 보기 위함이고, 사용자의 생성과 동시에 상세보기를 하기 위해서
       또다른 요청정보를 서버에 보내지 않더라도, 생성 작업의 결과로 상세정보를 위한 URL 값을 같이
       얻기 위함이다.(헤더에 URL 값이 찍히나?) 이로써 네트워크 비용이 발생하지 않게 된다.
     */

    //post put 은 클라이언트에서 json,xml같은 오브젝트로 받으려면 매개변수에
    //@requestBody로 요청을 받아야 한다.
    public ResponseEntity<Member> createUser(@Valid @RequestBody  Member user){
        Member savedUser = service.save(user);

        /*
         * ServletUriComponentsBuilder : 사용자 요청에 따른 작업을 처리 한 다음, 결과 값을 토대로 관련 URI를 생성해 주는 역할

         EX) /users 등록 작업을 요청했으니, 등록이 성공한 다음에는 클라이언트에게 성공 여부와
            상세정보 페이지에 대한 URI 값을 전달하기 위해 URI 객체 생성하는데 사용된다.
            생성된 URI객체는 클라이언트의 response header 또는 response body 에 포함하여 전달 할 수 있다.
         */
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();

        /*
           ResponseEntity 반환 객체 생성해서 created() 메소드는 반환 객체에 대한
           response 타입을 결정하는데, created로  할 경우 201코드를 반환 해준다.
           201 코드는 POST 요청과 같이 서버의 리소스를 추가 했을 때에 대한 응답 코드이며,
           201 응답 코드를 클라이언트 단에서 받게 되면, 정상 처리 되었다는 것을 알 수 있게 된다.
           success: ok와 같은 형식의 데이터를 받아서 처리할 필요가 없게 된다.(상황에 따라 다를듯)
           불필요한 네티워크 트래픽도 감소 된다.

           location의 URI는 response header를 보면 참조할 수 있다. 따라서, 클라이언트 단에서
           POST 요청에 대한 성공을 인지한 다음, 주소를 확인하면, 상세 정보 URI을 요청할 수 있게 된다.

         */
        return ResponseEntity.created(location).build();

    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable int id){
        Member user = service.deleteById(id);

        if(user == null){
            throw new UserNotFountException(String.format("ID[%s] not fount", id));
        }
    }

    @PutMapping("/users/{id}")
    public Member updateUser(@PathVariable int id, @RequestBody Member user){
        Member changeUser = service.updateById(id, user);

        if(changeUser == null){
            throw new UserNotFountException(String.format("ID[%s] not found", id));
        }

        return changeUser;
    }
}
