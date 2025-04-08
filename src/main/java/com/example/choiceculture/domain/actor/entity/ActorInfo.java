package com.example.choiceculture.domain.actor.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "actor_info")
public class ActorInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "festival_id", nullable = false)
    private Integer festivalId;

    @Size(max = 50)
    @Column(name = "actor_character", length = 50)
    private String actorCharacter;

    @Size(max = 10)
    @Column(name = "actor_name", length = 10)
    private String actorName;

    @Size(max = 255)
    @Column(name = "actor_image_url")
    private String actorImageUrl;

}