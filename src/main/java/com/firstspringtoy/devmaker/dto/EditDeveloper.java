package com.firstspringtoy.devmaker.dto;

import com.firstspringtoy.devmaker.type.DeveloperLevel;
import com.firstspringtoy.devmaker.type.DeveloperSkillType;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class EditDeveloper {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ToString
    public static class Request {

        @NotNull
        private DeveloperLevel developerLevel;
        @NotNull
        private DeveloperSkillType developerSkillType;
        @Min(0)
        @Max(20)
        @NotNull
        private Integer experienceYears;
    }
}
