package com.meowzip.fcm.repository;

import com.meowzip.fcm.entity.FcmToken;
import com.meowzip.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FcmTokenRepository extends JpaRepository<FcmToken, Long> {

    FcmToken findByToken(String token);

    FcmToken findByTokenAndMember(String token, Member member);
}
