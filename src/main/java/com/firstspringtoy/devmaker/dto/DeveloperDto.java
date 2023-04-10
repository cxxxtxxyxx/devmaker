package com.firstspringtoy.devmaker.dto;

import com.firstspringtoy.devmaker.entity.Developer;
import com.firstspringtoy.devmaker.type.DeveloperLevel;
import com.firstspringtoy.devmaker.type.DeveloperSkillType;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeveloperDto {

    private DeveloperLevel developerLevel;

    private DeveloperSkillType developerSkillType;
    private String memberId;


    public static DeveloperDto fromEntity(Developer developer) {
        return DeveloperDto.builder()
                .developerLevel(developer.getDeveloperLevel())
                .developerSkillType(developer.getDeveloperSkillType())
                .memberId(developer.getMemberId())
                .build();
    }

}
