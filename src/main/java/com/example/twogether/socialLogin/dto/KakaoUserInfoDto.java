package com.example.twogether.socialLogin.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class KakaoUserInfoDto {
    private Long kakaoId;
    private String nickname;
    private String email;
}
