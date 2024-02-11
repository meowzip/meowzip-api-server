package com.meowzip.coparent.repository;

import com.meowzip.coparent.entity.CoParent;
import com.meowzip.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoParentRepository extends JpaRepository<CoParent, Long> {
    List<CoParent> findAllByMember(Member member);
}
