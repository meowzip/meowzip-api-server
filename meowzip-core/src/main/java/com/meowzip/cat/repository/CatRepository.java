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

    @Query("select c from Cat c " +
            "left join c.coParents cp " +
            "where (c.member = :member or cp.participant = :member or cp.owner = :member) " +
            "and c.id in :ids " +
            "and cp.status = 'APPROVAL'"
    )
    List<Cat> findByMemberOrCoParentAndCatIdIn(@Param("member") Member member, @Param("ids") List<Long> ids);

    Optional<Cat> findByMemberAndId(Member member, Long id);

    int countByMember(Member member);

    @Query("SELECT c FROM Cat c JOIN FETCH c.member WHERE c.id = :catId")
    Optional<Cat> findByIdWithMember(@Param("catId") long catId);
}
