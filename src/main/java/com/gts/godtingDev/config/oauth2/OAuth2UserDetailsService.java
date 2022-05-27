package com.gts.godtingDev.config.oauth2;

import com.gts.godtingDev.config.oauth2.authentication.OAuth2UserDetails;
import com.gts.godtingDev.user.User;
import com.gts.godtingDev.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class OAuth2UserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        OAuth2UserDetails oAuth2UserDetails = (OAuth2UserDetails) SecurityContextHolder.getContext().getAuthentication();
        Optional<User> userEntity = userRepository.findByEmail(email);
        User user = userEntity.get();
        return new OAuth2UserDetails(user.getSocialType(), user.getSocialId(), user.getEmail(), (Set<GrantedAuthority>) oAuth2UserDetails.getAuthorities());
    }
}
