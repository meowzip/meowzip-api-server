package com.meowzip.resetpasswordtoken.repository;

import com.meowzip.resetpasswordtoken.entity.ResetPasswordToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResetPasswordTokenRepository extends CrudRepository<ResetPasswordToken, String> {

    Optional<ResetPasswordToken> findByMemberId(long memberId);
}
