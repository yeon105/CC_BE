package com.example.choiceculture.domain.actor.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class ActorInfoDTO {
    private Integer id;
    private String actorCharacter;
    private String actorName;
    private String profileImage;
}
