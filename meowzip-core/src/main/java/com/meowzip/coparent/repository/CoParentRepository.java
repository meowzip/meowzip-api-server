package com.meowzip.coparent.repository;

import com.meowzip.cat.entity.Cat;
import com.meowzip.coparent.entity.CoParent;
import com.meowzip.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CoParentRepository extends JpaRepository<CoParent, Long> {

    @Query("select cp from CoParent cp " +
            "where cp.participant = :participant " +
            "and cp.status = 'APPROVAL'")
    List<CoParent> findAllByParticipant(@Param("participant") Member participant);

    Optional<CoParent> findByParticipantAndId(Member participant, Long id);

    List<CoParent> findByCatAndOwnerAndParticipantIn(Cat cat,
                                                     Member owner,
                                                     List<Member> participants);

    Optional<CoParent> findByCatAndOwnerAndParticipant(Cat cat, Member me, Member requestedMember);
}
