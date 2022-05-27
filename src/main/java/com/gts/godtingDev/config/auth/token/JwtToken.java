package com.gts.godtingDev.config.auth.token;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "auth_refresh")
@Builder @Getter
@NoArgsConstructor
@AllArgsConstructor
public class JwtToken {

    @Id
    private String socialId;

    private String refreshToken;
}
