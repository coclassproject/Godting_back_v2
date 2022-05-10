package com.gts.godting.profile;

import com.gts.godting.user.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Builder @Getter
@NoArgsConstructor @AllArgsConstructor
public class Profile {

    @Id @GeneratedValue
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