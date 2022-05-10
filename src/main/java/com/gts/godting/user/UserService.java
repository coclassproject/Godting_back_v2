package com.gts.godting.user;

import com.gts.godting.config.exception.CustomException;
import com.gts.godting.config.exception.ExceptionMessage;
import com.gts.godting.config.redis.RedisUtil;
import com.gts.godting.mail.EmailMessage;
import com.gts.godting.mail.MailService;
import com.gts.godting.config.FileHandler;
import com.gts.godting.profile.Profile;
import com.gts.godting.profile.ProfileRepository;
import com.gts.godting.user.form.SignUpForm;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

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

    public void sendEmail(String email) {
        boolean emailCheck = userRepository.existsByEmail(email);
        //TODO 학교 웹메일 필터
        if (emailCheck) {
            throw new CustomException(ExceptionMessage.REDUPLICATION_EMAIL);
        }
        String emailCheckToken = UUID.randomUUID().toString();
        String oauth2Id = (String)redisUtil.get(emailCheckToken);
        if (oauth2Id != null) {
            redisUtil.delete(emailCheckToken);
        }
        redisUtil.set(emailCheckToken, email, 10);
        EmailMessage emailMessage = EmailMessage.builder()
                .to(email)
                .subject("Godting, 회원가입 이메일 인증")
                .message("인증 번호 박스에" + emailCheckToken + "을 입력하세요.").build();
        mailService.sendMail(emailMessage);
    }

    public void processEmailCheck(String emailCheckToken) throws TimeoutException {
        String oauth2Id = (String)redisUtil.get(emailCheckToken);
        if (oauth2Id == null) {
            throw new CustomException(ExceptionMessage.MAIL_AUTHORIZED_CODE_NOT_FOUND);
        }
    }

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
    }
}