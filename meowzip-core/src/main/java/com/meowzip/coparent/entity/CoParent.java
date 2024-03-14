package com.meowzip.coparent.entity;

import com.meowzip.cat.entity.Cat;
import com.meowzip.entity.BaseTimeEntity;
import com.meowzip.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CoParent extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participant_id")
    private Member participant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private Member owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cat_id")
    private Cat cat;

    @Enumerated(value = EnumType.STRING)
    private Status status;

    public boolean isParticipant(Member participant) {
        return this.participant.equals(participant);
    }

    public void accept() {
        this.status = Status.APPROVAL;
    }

    public enum Status {
        STANDBY,
        APPROVAL;
    }

    public boolean isApproval() {
        return status == Status.APPROVAL;
    }
}
