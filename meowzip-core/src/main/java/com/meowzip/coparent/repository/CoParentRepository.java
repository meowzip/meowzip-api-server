package com.meowzip.coparent.repository;

import com.meowzip.cat.entity.Cat;
import com.meowzip.coparent.entity.CoParent;
import com.meowzip.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CoParentRepository extends JpaRepository<CoParent, Long> {
    List<CoParent> findAllByOwner(Member owner);

    Optional<CoParent> findByParticipantAndId(Member participant, Long id);

    List<CoParent> findByCatAndOwnerAndParticipantIn(Cat cat,
                                                     Member owner,
                                                     List<Member> participants);

    Optional<CoParent> findByCatAndOwnerAndParticipant(Cat cat, Member me, Member requestedMember);
}
