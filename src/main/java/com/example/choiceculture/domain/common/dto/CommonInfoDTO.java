package com.example.choiceculture.domain.common.dto;

import com.example.choiceculture.domain.festival.enums.UseYn;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CommonInfoDTO {
    private String id;
    private String name;
    private UseYn useYn;
}
