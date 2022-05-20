package com.gts.godtingDev.user;

import com.gts.godtingDev.user.form.SignUpForm;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("user/email-check")
    public ResponseEntity validateEmailCheck(@RequestParam String emailCheckToken) {
        userService.processEmailCheck(emailCheckToken);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/user/register")
    public ResponseEntity registerUser(SignUpForm signUpForm) throws IOException {
        userService.saveNewUser(signUpForm);
        return ResponseEntity.ok().build();
    }

}
