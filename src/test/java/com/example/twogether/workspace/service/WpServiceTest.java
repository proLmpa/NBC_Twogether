package com.example.twogether.workspace.service;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;

import com.example.twogether.user.dto.LoginRequestDto;
import com.example.twogether.user.dto.SignupRequestDto;
import com.example.twogether.user.entity.User;
import com.example.twogether.user.entity.UserRoleEnum;
import com.example.twogether.user.repository.UserRepository;
import com.example.twogether.user.service.UserService;
import com.example.twogether.workspace.dto.WpRequestDto;
import com.example.twogether.workspace.dto.WpResponseDto;
import com.example.twogether.workspace.repository.WpRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class WpServiceTest {

    @Autowired
    private WpService wpService;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WpRepository wpRepository;
    private User user;

    @BeforeEach
    void signUp() {
        // given
        String email = "user2024@email.com";
        String password = "user123!@#";
        boolean admin = false;
        String adminToken = "";

        encoder = new BCryptPasswordEncoder();
        SignupRequestDto request = SignupRequestDto.builder().email(email).password(password)
            .admin(admin).adminToken(adminToken).build();

        // when
        User signed = userService.signup(request);

        // then
        user = signed;
    }


    @Test
    @DisplayName("워크스페이스 생성")
    public void createWorkspace() {
        String title = "Two Gether 1 !";
        String icon = "Two Gether 1 - icon 1 !";

        WpRequestDto wpRequestDto = WpRequestDto.builder()
            .title(title)
            .icon(icon)
            .build();

        // when
        WpResponseDto savedWp = wpService.createWorkspace(user, wpRequestDto);

        // then
        assertEquals(title, savedWp.getTitle());
        assertEquals(icon, savedWp.getIcon());
    }

    @Test
    @DisplayName("워크스페이스 수정")
    void editWorkspace() {
        //given
        String title = "새 워크스페이스";
        String icon = "새 아이콘";

        WpRequestDto wpRequestDto = WpRequestDto.builder()
            .title(title)
            .icon(icon)
            .build();

        wpService.createWorkspace(user, wpRequestDto);

        String editedTitle = "Two Gether 1 - 새 워크스페이스 1 !";
        String editedIcon = "Two Gether 1 - icon 1 - 새 아이콘 1 !";

        WpRequestDto wpRequestDto1 = new WpRequestDto(editedTitle, editedIcon);

        // when
        WpResponseDto editedWp = wpService.editWorkspace(user, 1L, wpRequestDto1);

        // then
        assertEquals(editedTitle, editedWp.getTitle());
        assertEquals(editedIcon, editedWp.getIcon());
    }

    @Test
    @DisplayName("워크스페이스 삭제")
    void deleteWorkspace() {
        //given
        String title = "Two Gether 1 !";
        String icon = "Two Gether 1 - icon 1 !";

        WpRequestDto wpRequestDto = WpRequestDto.builder()
            .title(title)
            .icon(icon)
            .build();

        wpService.createWorkspace(user, wpRequestDto);

        // when
        wpService.deleteWorkspace(user, 1L);

        // then
        Assertions.assertNull(wpRepository.findById(anyLong()).orElse(null));
    }

    @Test
    @DisplayName("워크스페이스 단일 조회")
    void getWorkspace() {
        //given
        String title = "Two Gether 1 !";
        String icon = "Two Gether 1 - icon 1 !";

        WpRequestDto wpRequestDto = WpRequestDto.builder()
            .title(title)
            .icon(icon)
            .build();

        wpService.createWorkspace(user, wpRequestDto);

        // when
        wpService.getWorkspace(1L);

        // then
        Assertions.assertNull(wpRepository.findById(anyLong()).orElse(null));
    }

    @Test
    @DisplayName("워크스페이스 전체 조회")
    public void getWorkspaces() {
        //given
        String title1 = "Two Gether 1 !";
        String icon1 = "Two Gether 1 - icon 1 !";

        WpRequestDto wpRequestDto1 = WpRequestDto.builder()
            .title(title1)
            .icon(icon1)
            .build();

        WpResponseDto wpResponseDto1 = wpService.createWorkspace(user, wpRequestDto1);

        String title2 = "Two Gether 2 !";
        String icon2 = "Two Gether 2 - icon 2 !";

        WpRequestDto wpRequestDto2 = WpRequestDto.builder()
            .title(title2)
            .icon(icon2)
            .build();

        WpResponseDto wpResponseDto2 = wpService.createWorkspace(user, wpRequestDto2);

        String title3 = "Two Gether 3 !";
        String icon3 = "Two Gether 3 - icon 3 !";

        WpRequestDto wpRequestDto3 = WpRequestDto.builder()
            .title(title3)
            .icon(icon3)
            .build();

        WpResponseDto wpResponseDto3 = wpService.createWorkspace(user, wpRequestDto3);

        // when
        wpService.getWorkspaces(user);

        // then
        assertEquals(wpRepository.findAll().size(), 3);
        assertEquals(wpRequestDto1.getTitle(), wpResponseDto1.getTitle());
        assertEquals(wpRequestDto2.getTitle(), wpResponseDto2.getTitle());
        assertEquals(wpRequestDto3.getTitle(), wpResponseDto3.getTitle());
    }
}
