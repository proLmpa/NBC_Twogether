package com.example.twogether.oauth2;

import com.example.twogether.user.entity.User;
import java.io.Serializable;
import lombok.Getter;

@Getter
public class SessionUser implements Serializable {
    private String name;
    private String email;
    private String introduction;

    public SessionUser(User user){
        this.name = user.getNickname();
        this.email = user.getEmail();
        this.introduction = user.getIntroduction();
    }
}