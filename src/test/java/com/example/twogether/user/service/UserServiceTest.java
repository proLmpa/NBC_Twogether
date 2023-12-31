package com.example.twogether.user.service;

import com.example.twogether.common.error.CustomErrorCode;
import com.example.twogether.common.exception.CustomException;
import com.example.twogether.user.dto.EditPasswordRequestDto;
import com.example.twogether.user.dto.EditUserRequestDto;
import com.example.twogether.user.entity.User;
import com.example.twogether.user.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserServiceTest {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder encoder;

    private User user;

    @BeforeEach
    void init() {
        user = userRepository.findById(1L).orElse(null);
    }

    @Test
    @DisplayName("사용자 정보 수정")
    void editUserInfo() {
        // given
        String nickname = "I'm Owl";
        String introduction = "This is my child owo";

        EditUserRequestDto requestDto = new EditUserRequestDto(nickname, introduction);

        // when
        User edited = userService.editUserInfo(requestDto, user);

        // then
        Assertions.assertEquals(nickname, edited.getNickname());
        Assertions.assertEquals(introduction, edited.getIntroduction());
    }

    @Test
    @DisplayName("사용자 정보 삭제")
    void deleteUserInfo() {
        // given
        long userId = 1L;

        // when
        userService.deleteUserInfo(userId, user);

        // then
        Assertions.assertNull(userRepository.findById(userId).orElse(null));
    }

    @Test
    @DisplayName("사용자 비밀번호 수정")
    void editUserPassword() {
        // given
        String password = "user123!@#";
        String newPassword = "user234@#$";

        EditPasswordRequestDto requestDto = EditPasswordRequestDto.builder().password(password)
            .newPassword(newPassword).build();

        // when - 정상 비밀번호 수정
        User edited = userService.editUserPassword(requestDto, user);

        // then
        Assertions.assertFalse(encoder.matches(password, edited.getPassword()));
        Assertions.assertTrue(encoder.matches(newPassword, edited.getPassword()));
    }

    @Test
    @DisplayName("사용자 비밀번호 수정 실패")
    void editUserPasswordFailed() {
        // given
        String password = "user123!@#";
        String newPassword = "user234@#$";

        EditPasswordRequestDto recentlyUsed = EditPasswordRequestDto.builder()
            .password(password)
            .newPassword(password).build();

        EditPasswordRequestDto wrongPassword = EditPasswordRequestDto.builder()
            .password(newPassword)
            .newPassword(newPassword).build();

        // when - then(1) : 최근 사용한 비밀번호 사용 불가
        try {
            userService.editUserPassword(recentlyUsed, user);
        } catch (CustomException e) {
            Assertions.assertEquals(CustomErrorCode.PASSWORD_RECENTLY_USED, e.getErrorCode());
        }

        // when - then(2) : 잘못된 비밀번호 입력
        try {
            userService.editUserPassword(wrongPassword, user);
        } catch (CustomException e) {
            Assertions.assertEquals(CustomErrorCode.PASSWORD_MISMATCHED, e.getErrorCode());
        }
    }
}
