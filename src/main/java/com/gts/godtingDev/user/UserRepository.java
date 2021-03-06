package com.gts.godtingDev.user;

import com.gts.godtingDev.user.oauth2.SocialType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findBySocialTypeAndSocialId(SocialType socialType, String socialId);

    Optional<User> findByEmail(String email);

    Optional<User> findBySocialId(String socialId);

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

}
