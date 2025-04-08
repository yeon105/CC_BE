package com.example.choiceculture.domain.review.entity;

import com.example.choiceculture.domain.festival.enums.ReviewType;
import com.example.choiceculture.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(name = "review_info")
public class ReviewInfo extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "festival_id", nullable = false)
    private Integer festivalId;

    @Size(max = 50)
    @NotNull
    @ColumnDefault("''")
    @Column(name = "member_id", nullable = false, length = 50)
    private String memberId;

    @Size(max = 50)
    @Column(name = "title", length = 50)
    private String title;

    @Size(max = 255)
    @Column(name = "content")
    private String content;

    @Column(name = "rating")
    private Integer rating;

    @Enumerated(EnumType.STRING)
    private ReviewType type;

}