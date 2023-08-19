package com.example.twogether.mail.service;

import com.example.twogether.common.error.CustomErrorCode;
import com.example.twogether.common.exception.CustomException;
import com.example.twogether.common.util.RedisUtil;
import com.example.twogether.user.entity.User;
import com.example.twogether.user.entity.UserRoleEnum;
import com.example.twogether.user.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EmailCerificationService {

    private final UserRepository userRepository;
    private final JavaMailSender emailSender;
    private final RedisUtil redisUtil;

    private static final String DOMAIN_NAME = "http://localhost:8080";

    // 인증 요청 메일 전송
    public void sendEmailForCertification(String email)
        throws NoSuchAlgorithmException, MessagingException {
        String certificationNumber = getVertificationNumber();
        String content = String.format(
            "%s/api/users/verify?certificationNumber=%s&email=%s 링크를 3분 이내에 클릭해주세요.",
            DOMAIN_NAME, certificationNumber, email);
        sendMail(email, content);
        redisUtil.saveCertificationNumber(email, certificationNumber);
    }

    // 인증번호 생성기
    private static String getVertificationNumber() throws NoSuchAlgorithmException {
        String result;

        do {
            int i = SecureRandom.getInstanceStrong().nextInt(999999);
            result = String.valueOf(i);
        } while (result.length() != 6);

        return result;
    }

    private void sendMail(String email, String content) throws MessagingException {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setTo(email);
        helper.setSubject("Twogether 이메일 인증 요청입니다.");
        helper.setText(content);
        emailSender.send(mimeMessage);
    }

    @Transactional
    public void verifyEmail(String certificationNumber, String email) {
        verifyCertification(certificationNumber, email);

        redisUtil.removeCertificationNumber(email);

        User user = userRepository.findByEmail(email).orElseThrow(() ->
            new CustomException(CustomErrorCode.USER_NOT_FOUND));

        user.editRole(UserRoleEnum.USER);
    }

    private void verifyCertification(String certificationNumber, String email) {
        if(!redisUtil.hasKey(email))
            throw new CustomException(CustomErrorCode.EMAIL_NOT_FOUND);

        if(!Objects.equals(redisUtil.getCertificationNumber(email), certificationNumber))
            throw new CustomException(CustomErrorCode.VERIFY_NOT_ALLOWED);
    }
}
