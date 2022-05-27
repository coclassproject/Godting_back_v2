package com.gts.godtingDev.profile;

import com.gts.godtingDev.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Profile {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private User user;

    private String path;

    private String name;

    private String save_name;

    private Long fileSize;

    private LocalDateTime create_date_time;

    public void setUser(User user) {
        this.user = user;
    }
}