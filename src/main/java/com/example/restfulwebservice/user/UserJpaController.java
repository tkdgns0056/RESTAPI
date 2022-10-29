package com.example.restfulwebservice.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.ControllerLinkRelationProvider;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/jpa")
public class UserJpaController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    // http://localhost:8088/jpa/users
    @GetMapping("/users")
    public List<Member> retrieveAllUsers(){

        return userRepository.findAll();
    }

    @GetMapping("/users/{id}")
    public EntityModel<Member> retrieveUser(@PathVariable  int id){

        /* 유저가 존재 하지도 않을 수 있기 때문에 Optional 타입으로 만듦 */
        Optional<Member> user = userRepository.findById(id);

        if(!user.isPresent()){
            throw new UserNotFountException(String.format("ID[%s] not found", id));
        }
        //HATEOAS 이용
        EntityModel<Member> model = EntityModel.of(user.get());
        WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).retrieveAllUsers());
        model.add(linkTo.withRel("all-users"));

        return model;
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable int id){
        userRepository.deleteById(id);
    }

    @PostMapping("/users")
    public ResponseEntity<Member> saveUser(@Valid @RequestBody Member member){
        Member saveUser = userRepository.save(member);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saveUser.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    // /jpa/users/90001/posts
    @GetMapping("/users/{id}/posts")
    public List<Post> retrieveAllPostsByUser(@PathVariable int id){
        /* 유저가 존재 하지도 않을 수 있기 때문에 Optional 타입으로 만듦 */
        Optional<Member> user = userRepository.findById(id);

        if(!user.isPresent()){
            throw new UserNotFountException(String.format("ID[%s] not found", id));
        }

        return user.get().getPosts();
    }


    /*
        JPA 이용한 새 게시물 추가
     */
    @PostMapping("/users/{id}/posts")
    public ResponseEntity<Post> createPost(@PathVariable int id, @RequestBody Post post){

        //사용자 정보를 알아내서 저장 해줘야함.
        /* 유저가 존재 하지도 않을 수 있기 때문에 Optional 타입으로 만듦 */
        Optional<Member> user = userRepository.findById(id);

        if(!user.isPresent()){
            throw new UserNotFountException(String.format("ID[%s] not found", id));
        }
        //get으로 유저 정보 가져와서 사용자 정보 지정
        post.setMember(user.get());
        //그리고 그 정보를 담아줌.
        Post savedPost = postRepository.save(post);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedPost.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }
}
