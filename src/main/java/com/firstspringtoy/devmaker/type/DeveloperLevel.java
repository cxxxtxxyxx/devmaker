package com.firstspringtoy.devmaker.type;


import com.firstspringtoy.devmaker.exception.DevmakerException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.function.Function;

import static com.firstspringtoy.devmaker.constant.DevmakerConstant.MAX_JUNIOR_EXPERIENCE_YEARS;
import static com.firstspringtoy.devmaker.constant.DevmakerConstant.MIN_SENIOR_EXPERIENCE_YEARS;
import static com.firstspringtoy.devmaker.exception.DevmakerErrorCode.LEVEL_EXPERIENCE_YEARS_NOT_MATCHED;

@AllArgsConstructor
@Getter
public enum DeveloperLevel {
    NEW("신입 개발자", years -> years == 0),
    JUNIOR("주니어 개발자", years -> years <= MAX_JUNIOR_EXPERIENCE_YEARS),
    JUNGNIOR("중니어 개발자", years -> MAX_JUNIOR_EXPERIENCE_YEARS < years && years < MIN_SENIOR_EXPERIENCE_YEARS),
    SENIOR("시니어 개발자", years -> MIN_SENIOR_EXPERIENCE_YEARS <= years);

    private final String description;
    private final Function<Integer, Boolean> validateFunction;

    public void validateExperienceYears(Integer years) {
        if (!validateFunction.apply(years)) {
            throw new DevmakerException(LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
        }
    }

}
