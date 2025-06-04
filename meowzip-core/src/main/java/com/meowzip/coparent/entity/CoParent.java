package com.meowzip.coparent.entity;

import com.meowzip.cat.entity.Cat;
import com.meowzip.entity.BaseTimeEntity;
import com.meowzip.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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

    private LocalDateTime acceptableDatetime;

    public boolean isParticipant(Member participant) {
        return this.participant.equals(participant) && this.status == Status.APPROVAL;
    }

    public boolean isParticipant(Cat cat, Member participant) {
        return this.cat.equals(cat) && this.participant.equals(participant);
    }

    public void accept() {
        this.status = Status.APPROVAL;
    }

    public boolean isStandBy() {
        return status == Status.STANDBY;
    }

    public boolean isRejected() {
        return status == Status.REJECT;
    }

    public void reject() {
        this.status = Status.REJECT;
    }

    public enum Status {
        STANDBY,
        APPROVAL,
        REJECT,
        CANCELED
        ;
    }

    public boolean isApproval() {
        return status == Status.APPROVAL;
    }

    public boolean isExpired() {
        return acceptableDatetime.isBefore(LocalDateTime.now());
    }

    public void cancel() {
        this.status = Status.CANCELED;
    }

    public boolean isCanceled() {
        return status == Status.CANCELED;
    }
}
