package com.example.choiceculture.domain.common.entity;

import com.example.choiceculture.domain.festival.enums.UseYn;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "common_info")
public class CommonInfo {
    @Id
    @Size(max = 4)
    @Column(name = "id", nullable = false, length = 4)
    private String id;

    @Size(max = 50)
    @Column(name = "name", length = 50)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "use_yn")
    private UseYn useYn;

}