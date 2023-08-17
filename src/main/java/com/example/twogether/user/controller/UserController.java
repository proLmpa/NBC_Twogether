package com.example.twogether.user.controller;

import com.example.twogether.common.dto.ApiResponseDto;
import com.example.twogether.common.security.UserDetailsImpl;
import com.example.twogether.user.dto.EditUserRequestDto;
import com.example.twogether.user.dto.LoginRequestDto;
import com.example.twogether.user.dto.SignupRequestDto;
import com.example.twogether.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@Tag(name = "사용자 API", description = "사용자의 회원 가입 기능과 관련된 API 정보를 담고 있습니다.")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "회원 가입", description = "SignupRequesetDto를 통해 회원이 제출한 정보의 유효성 검사 후 통과 시 DB에 저장하고 성공 메시지를 반환합니다.")
    @PostMapping("/signup")
    public ResponseEntity<ApiResponseDto> signup(
        @Validated @RequestBody SignupRequestDto requestDto) {
        userService.signup(requestDto);
        return ResponseEntity.ok().body(new ApiResponseDto(HttpStatus.OK.value(), "회원 가입 성공"));
    }

    @Operation(summary = "로그인", description = "LoginRequestDto을 DB에 저장된 사용자 정보와 비교하여 동일할 시 성공 메시지를 반환합니다.")
    @PostMapping("/login")
    public ResponseEntity<ApiResponseDto> login(@RequestBody LoginRequestDto requestDto) {
        return null;
    }

    @Operation(summary = "사용자 정보 수정", description = "전달된 Bearer 토큰을 통해 본인 확인 후 EditUserRequestDto를 통해 해당 사용자의 일부 정보를 수정합니다.")
    @PatchMapping("/info")
    public ResponseEntity<ApiResponseDto> editUserInfo(@RequestBody EditUserRequestDto requestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.editUserInfo(requestDto, userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponseDto(HttpStatus.OK.value(), "회원 정보 수정 성공"));
    }

    @Operation(summary = "회원 탈퇴", description = "전달된 Bearer 토큰을 통해 본인 혹은 관리자 여부 확인 후 사용자의 이메일로 찾은 회원이 탈퇴됩니다.")
    @DeleteMapping("/{id}/signout")
    public ResponseEntity<ApiResponseDto> deleteUserInfo(@PathVariable Long id,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.deleteUserInfo(id, userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponseDto(HttpStatus.OK.value(), "회원 탈퇴 성공"));
    }

//    @Operation(summary = "사용자 비밀번호 수정", description = "전달된 Bearer 토큰을 통해 본인 확인 후 EditPasswordRequestDto를 통해 해당 사용자의 비밀번호를 수정합니다.")
//    @PatchMapping("/password")
//    public ResponseEntity<ApiResponseDto> editUserPassword(@Valid @RequestBody EditPasswordRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
//        userService.editUserPassword(requestDto, userDetails.getUser());
//        return ResponseEntity.ok().body(new ApiResponseDto(HttpStatus.OK.value(), "회원 비밀번호 수정 성공"));
//    }
}