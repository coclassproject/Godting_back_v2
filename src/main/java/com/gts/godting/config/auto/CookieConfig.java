package com.gts.godting.config.auto;

import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Service
public class CookieConfig {

    private Long cookieValidTime = 1000L * 60L * 24L * 60L * 30L;

    public Cookie createCookie(String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setPath("/token");
        cookie.setSecure(true);
        cookie.setMaxAge(Long.valueOf(cookieValidTime).intValue());
        return cookie;
    }

    public Cookie getCookie(HttpServletRequest request, String name) {
        final Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }

        for (Cookie cookie : cookies) {
            if(cookie.getName().equals(name)) {
                return cookie;
            }
        }

        return null;
    }
}
