package com.gts.godtingDev.user;

import com.gts.godtingDev.profile.Profile;
import com.gts.godtingDev.user.oauth2.Role;
import com.gts.godtingDev.user.oauth2.SocialType;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Builder @Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String password;

    @Column(unique = true)
    private String nickname;

    private String gender;

    private String region;

    private Integer height;

    private Integer studentNum;

    private Integer age;

    private String lecture;

    private String interest;

    @Column
    private boolean smoking;

    @Column
    private boolean drinking;

    @Column
    private boolean duty;

    @OneToMany(mappedBy = "user")
    private Set<Profile> profiles = new HashSet<>();

    @Column(unique = true)
    private String socialId;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(unique = true)
    private String email;

    private LocalDateTime create_date_time;

    public void completeSignUp() {
        this.create_date_time = LocalDateTime.now();
    }

    public void addProfile(Profile profile) {
        this.profiles.add(profile);
        profile.setUser(this);
    }
}
