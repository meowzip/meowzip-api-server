package com.meowzip.member.entity;

import com.meowzip.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Entity
@Table
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String email;
    private String password;

    @Column(name = "login_type")
    @Enumerated(value = EnumType.STRING)
    private LoginType loginType;

    private String nickname;

    @Column(name = "profile_image")
    private String profileImage;

    @Enumerated(value = EnumType.STRING)
    private Status status;

    @Column(name = "withdrew_at")
    private LocalDateTime withdrewAt;

    public void encodePassword(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(password);
    }

    public enum Status {
        ACTIVE, WITHDRAWAL
    }
}
