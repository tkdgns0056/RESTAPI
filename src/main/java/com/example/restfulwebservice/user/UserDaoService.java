package com.example.restfulwebservice.user;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Service
public class UserDaoService {
    private static List<Member> users = new ArrayList<>();

    private static int userCount = 3;

    static{
        users.add(new Member(1,"hoon",new Date(), "test1", "701010-1111111"));
        users.add(new Member(2,"ki",new Date(),"test2", "801010-2222222"));
        users.add(new Member(3,"hyuk",new Date(),"test3", "901010-1111111"));
    }

    public List<Member> findAll(){

        return users;
    }

    public Member save(Member user){
        if(user.getId() == null){
            user.setId(++userCount);
        }
        users.add(user);
        return user;
    }

    public Member findOne(int id){
        for(Member user : users){
            if(user.getId() == id){
                return user;
            }
        }
        return null;
    }

    public Member deleteById(int id){
        //열거형 타입의 데이터 사용
        Iterator<Member> iterator = users.iterator();

        while(iterator.hasNext()){
            Member user = iterator.next();

            if(user.getId() == id){
                iterator.remove();
                return user;
            }
        }

        return null;
    }

    public Member updateById(int id, Member updateUser){
        for(Member user : users){
            if(user.getId() == id){
                user.setName(updateUser.getName());
                return user;
            }

        }
        return null;
    }
}
