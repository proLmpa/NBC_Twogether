package com.example.twogether.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
public class KakaoUserInfoDto {
    private Long kakaoId;
    private String nickname;
    private String email;
}
