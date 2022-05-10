package com.gts.godting.user;

import com.gts.godting.profile.Profile;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Builder @Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String password;

    @Column(unique = true)
    private String nickname;

    private String gender;

    private String region;

    private int height;

    private int studentNum;

    private int age;

    private String lecture;

    private String interest;

    private boolean smoking;

    private boolean drinking;

    private boolean duty;

    @OneToMany(mappedBy = "user")
    private Set<Profile> profiles = new HashSet<>();

    @Column(unique = true)
    private String oauth2Id;

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