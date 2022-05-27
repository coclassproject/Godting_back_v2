package com.gts.godtingDev.user;

import com.gts.godtingDev.config.auto.FileHandler;
import com.gts.godtingDev.config.exception.CustomException;
import com.gts.godtingDev.config.exception.ExceptionMessage;
import com.gts.godtingDev.config.mail.EmailMessage;
import com.gts.godtingDev.config.mail.MailService;
import com.gts.godtingDev.config.redis.RedisUtil;
import com.gts.godtingDev.profile.Profile;
import com.gts.godtingDev.profile.ProfileRepository;
import com.gts.godtingDev.user.form.SignUpForm;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final RedisUtil redisUtil;
    private final MailService mailService;
    private final FileHandler fileHandler;
    private final ProfileRepository profileRepository;

    public void saveNewUser(SignUpForm signUpForm) throws IOException {

        if (!signUpForm.getEmailCheck()) {
            throw new CustomException(ExceptionMessage.UNAUTHORIZED_EMAIL);
        }

        boolean emailCheck = userRepository.existsByEmail(signUpForm.getEmail());
        if (emailCheck) {
            throw new CustomException(ExceptionMessage.REDUPLICATION_EMAIL);
        }
        boolean nicknameCheck = userRepository.existsByNickname(signUpForm.getNickname());
        if (nicknameCheck) {
            throw new CustomException(ExceptionMessage.REDUPLICATION_NICKNAME);
        }
        User user = modelMapper.map(signUpForm, User.class);
        user.completeSignUp();

        List<Profile> profileList = fileHandler.parseFile(signUpForm.getProfiles());

        if (!profileList.isEmpty()) {
            for (Profile profile : profileList) {
                user.addProfile(profileRepository.save(profile));
            }
        }

        userRepository.save(user);
        sendEmail(user.getEmail());
    }

    public void sendEmail(String email) {
        boolean emailCheck = userRepository.existsByEmail(email);

        // 자바 기반 웹메일 필터
        int index = email.indexOf("@");
        String domain = email.substring(index + 1);
        if (!(domain.equals("bu.ac.kr"))) {
            throw new CustomException(ExceptionMessage.CANNOT_USE_DOMAIN);
        }
        // 이메일 중복여부
        if (emailCheck) {
            throw new CustomException(ExceptionMessage.REDUPLICATION_EMAIL);
        }

        // 이메일 토큰생성(랜덤)
        String emailCheckToken = UUID.randomUUID().toString();

        // 랜덤 토큰 생성 후 Redis에 담고 가져오기
        String socialId = (String)redisUtil.get(emailCheckToken);
        if (socialId != null) {
            redisUtil.delete(emailCheckToken);
        }
        redisUtil.set(emailCheckToken, email, 10);
        EmailMessage emailMessage = EmailMessage.builder()
                .to(email)
                .subject("Godting 회원가입 이메일 인증")
                .message("인증 번호 박스에" + emailCheckToken + "을 입력하세요.").build();
        mailService.sendMail(emailMessage);
    }

    public void processEmailCheck(String emailCheckToken) {
        String socialId = (String) redisUtil.get(emailCheckToken);
        if (socialId == null) {
            throw new CustomException(ExceptionMessage.MAIL_AUTHORIZED_CODE_NOT_FOUND);
        }
    }

    // TODO : ??
}
