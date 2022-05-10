package com.gts.godting.user.form;

import lombok.Getter;
import java.util.Set;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Set;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
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

    private String oauth2Id;

    //TODO 이미지
    private List<MultipartFile> profiles;

}
