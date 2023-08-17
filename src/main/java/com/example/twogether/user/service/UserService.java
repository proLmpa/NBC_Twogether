package com.example.twogether.user.service;

import com.example.twogether.common.error.CustomErrorCode;
import com.example.twogether.common.exception.CustomException;
import com.example.twogether.user.dto.EditPasswordRequestDto;
import com.example.twogether.user.dto.EditUserRequestDto;
import com.example.twogether.user.dto.SignupRequestDto;
import com.example.twogether.user.entity.User;
import com.example.twogether.user.entity.UserPassword;
import com.example.twogether.user.entity.UserRoleEnum;
import com.example.twogether.user.repository.UserPasswordRepository;
import com.example.twogether.user.repository.UserRepository;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserPasswordRepository userPasswordRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.token}")
    private String adminToken;

    @Transactional
    public User signup(SignupRequestDto requestDto) {
        String email = requestDto.getEmail();
        String password = passwordEncoder.encode(requestDto.getPassword());

        // 사용자 존재 여부 확인
        findExistingUserByEmail(email);

        UserRoleEnum role = UserRoleEnum.USER;
        if (requestDto.isAdmin() && requestDto.getAdminToken().equals(adminToken)) {
            role = UserRoleEnum.ADMIN;
        }

        User user = userRepository.save(requestDto.toEntity(password, role));
        userPasswordRepository.save(UserPassword.builder().password(password).user(user).build());
        return user;
    }

    @Transactional
    public User editUserInfo(EditUserRequestDto requestDto, User user) {
        User found = findUser(user.getId());

        found.editUserInfo(requestDto.getNickname(), requestDto.getIntroduction());
        return found;
    }

    @Transactional
    public void deleteUserInfo(Long id, User user) {
        User found = findUser(id);
        confirmUser(found, user);

        userPasswordRepository.deleteAllByUser_Id(found.getId());
        userRepository.deleteById(found.getId());
    }

    @Transactional
    public User editUserPassword(EditPasswordRequestDto requestDto, User user) {
        User found = findUser(user.getId());

        checkPassword(requestDto.getPassword(), found.getPassword());
        checkRecentPasswords(found.getId(), requestDto.getNewPassword());

        String newPassword = passwordEncoder.encode(requestDto.getNewPassword());
        userPasswordRepository.save(UserPassword.builder().password(newPassword).user(found).build());
        found.editPassword(newPassword);

        return found;
    }

    private void findExistingUserByEmail(String email) {
        if (userRepository.findByEmail(email).orElse(null) != null) {
            throw new CustomException(CustomErrorCode.USER_ALREADY_EXISTS);
        }
    }

    private User findUser(long id) {
        return userRepository.findById(id).orElseThrow(() ->
            new CustomException(CustomErrorCode.USER_NOT_FOUND));
    }

    private void confirmUser(User user1, User user2) {
        if (!Objects.equals(user1.getId(), user2.getId())
            && !user2.getRole().equals(UserRoleEnum.ADMIN)) {
            throw new CustomException(CustomErrorCode.UNAUTHORIZED_REQUESET);
        }
    }

    private void checkPassword(String inputPassword, String userPassword) {
        if (!passwordEncoder.matches(inputPassword, userPassword)) {
            throw new CustomException(CustomErrorCode.PASSWORD_MISMATCHED);
        }
    }

    private void checkRecentPasswords(Long userId, String newPassword) {
        List<UserPassword> userPasswords = userPasswordRepository.get3RecentPasswords(userId);
        userPasswords.forEach(password -> {
            if (passwordEncoder.matches(newPassword, password.getPassword()))
                throw new CustomException(CustomErrorCode.PASSWORD_RECENTLY_USED);
        });
    }
}