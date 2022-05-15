package com.gts.godtingDev.user.form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpForm {

    private String email;

    private Boolean emailCheck;

    private String nickname;

    private String gender;

    private String region;

    private int height;

    private int studentNum;

    private int age;

    private String lecture;

    private Set<String> interest;

    private boolean smoking;

    private boolean drinking;

    private boolean duty;

    private String socialId;

    private List<MultipartFile> profiles;

}
