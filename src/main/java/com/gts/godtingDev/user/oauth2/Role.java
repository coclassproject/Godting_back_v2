package com.gts.godtingDev.user.oauth2;

import lombok.Getter;

@Getter
public enum Role {

    GUEST("ROLE_GUEST"),
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN");

    private String gratedAuthority;

    Role(String gratedAuthority) {
        this.gratedAuthority = gratedAuthority;
    }
}
