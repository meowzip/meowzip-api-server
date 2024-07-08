package com.meowzip.cat.repository;

import com.meowzip.cat.entity.Cat;
import com.meowzip.member.entity.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CatRepository extends JpaRepository<Cat, Long> {

    List<Cat> findAllByMemberOrderByCreatedAtAsc(Member member, Pageable pageable);

    @Query("select c from Cat c where c.member = :member and c.id in :ids")
    List<Cat> findByMemberAndCatIdIn(@Param("member") Member member, @Param("ids") List<Long> ids);

    Optional<Cat> findByMemberAndId(Member member, Long id);

    int countByMember(Member member);
}
