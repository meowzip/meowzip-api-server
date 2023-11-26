package com.meowzip.member.repository;

import com.meowzip.member.entity.ForbiddenNickname;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ForbiddenNicknameRepository extends JpaRepository<ForbiddenNickname, String> {

    @Query(value = "select nickname from forbidden_nickname", nativeQuery = true)
    Set<String> findAllNicknames();
}
