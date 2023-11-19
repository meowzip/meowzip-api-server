package com.meowzip.resetpasswordtoken.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash(value = "reset_password_token", timeToLive = 60 * 30)
public class ResetPasswordToken {

    @Id
    @GeneratedValue
    private UUID id;

    @Indexed
    private long memberId;
}
