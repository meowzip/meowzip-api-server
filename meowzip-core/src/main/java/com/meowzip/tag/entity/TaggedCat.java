package com.meowzip.tag.entity;

import com.meowzip.cat.entity.Cat;
import com.meowzip.diary.entity.Diary;
import com.meowzip.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TaggedCat extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cat_id")
    private Cat cat;

    @ManyToOne
    @JoinColumn(name = "diary_id")
    private Diary diary;

    public static TaggedCat create(Cat cat, Diary diary) {
        return TaggedCat.builder()
                .cat(cat)
                .diary(diary)
                .build();
    }
}
