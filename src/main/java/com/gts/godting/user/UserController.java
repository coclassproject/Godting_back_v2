package com.gts.godting.user;

import com.gts.godting.config.auth.token.JwtTokenProvider;
import com.gts.godting.config.auto.CookieConfig;
import com.gts.godting.user.form.SignUpForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final CookieConfig cookieConfig;

    @Value("${auth.login}")
    private String loginUrl;

    @GetMapping("/auth/kakao")
    public void loginByKakao(HttpServletResponse response) throws IOException {
        response.sendRedirect(loginUrl + "/kakao");
    }

    @GetMapping("/auth/naver")
    public void loginByNaver(HttpServletResponse response) throws IOException {
        response.sendRedirect(loginUrl + "/naver");
    }

    @GetMapping("/auth/google")
    public void loginByGoogle(HttpServletResponse response) throws IOException {
        response.sendRedirect(loginUrl + "/google");
    }

    @GetMapping("/test")
    public String test() {
        return "회원가입 리다이렉트 테스트";
    }

    @PostMapping("/user/send-email")
    public ResponseEntity sendEmailCheck(@RequestParam String email) {
        userService.sendEmail(email);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/user/check-email")
    public ResponseEntity validateEmailCheck(@RequestParam String emailCheckToken) throws TimeoutException {
        userService.processEmailCheck(emailCheckToken);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/register")
    public ResponseEntity registerUser(SignUpForm signUpForm) throws IOException {
        log.info("{}, {}, {}", signUpForm.getEmail(), signUpForm.getNickname(), signUpForm.getOauth2Id());
        userService.saveNewUser(signUpForm);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/login")
    public ResponseEntity login(@RequestParam String oauth2Id) {
        User user = userRepository.findByOauth2Id(oauth2Id);
        String accessToken = jwtTokenProvider.accessToken(user.getEmail());
        Cookie cookie = cookieConfig.createCookie("refreshToken", jwtTokenProvider.refreshToken(user.getEmail()));

        return ResponseEntity.ok().build(); // 수정 필요, 엑세스는 서버 저장, 리프레시는 쿠키로 response, user는 객체로 내보내서 마이페이지 구성
    }
 }