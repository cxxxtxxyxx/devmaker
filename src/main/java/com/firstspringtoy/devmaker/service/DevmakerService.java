package com.firstspringtoy.devmaker.service;


import com.firstspringtoy.devmaker.code.StatusCode;
import com.firstspringtoy.devmaker.dto.CreateDeveloper;
import com.firstspringtoy.devmaker.dto.DeveloperDetailDto;
import com.firstspringtoy.devmaker.dto.DeveloperDto;
import com.firstspringtoy.devmaker.dto.EditDeveloper;
import com.firstspringtoy.devmaker.entity.Developer;
import com.firstspringtoy.devmaker.entity.RetiredDeveloper;
import com.firstspringtoy.devmaker.exception.DevmakerException;
import com.firstspringtoy.devmaker.repository.DeveloperRepository;
import com.firstspringtoy.devmaker.repository.RetiredDeveloperRepository;
import com.firstspringtoy.devmaker.type.DeveloperLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.firstspringtoy.devmaker.code.StatusCode.EMPLOYED;
import static com.firstspringtoy.devmaker.exception.DevmakerErrorCode.*;

@Service
@RequiredArgsConstructor
public class DevmakerService {
    private final DeveloperRepository developerRepository;
    private final RetiredDeveloperRepository retiredDeveloperRepository;

    @Transactional
    public CreateDeveloper.Response createDeveloper(CreateDeveloper.Request request) {

        validateCreateDeveloperRequest(request);


        Developer developer = new Developer().builder()
                .developerLevel(request.getDeveloperLevel())
                .developerSkillType(request.getDeveloperSkillType())
                .experienceYears(request.getExperienceYears())
                .memberId(request.getMemberId())
                .name(request.getName())
                .age(request.getAge())
                .statusCode(EMPLOYED)
                .build();

        developerRepository.save(developer);


        return CreateDeveloper.Response.fromEntity(developer);
    }

    public List<DeveloperDto> getAllEmployedDevelopers() {
        return developerRepository.findDeveloperByStatusCodeEquals(EMPLOYED)
                .stream()
                .map(DeveloperDto::fromEntity)
                .collect(Collectors.toList());
    }

    public DeveloperDetailDto getDeveloperDetail(String memberId) {
        return developerRepository.findByMemberId(memberId)
                .map(DeveloperDetailDto::fromEntity)
                .orElseThrow(() -> new DevmakerException(NO_DEVELOPER));
    }


    @Transactional
    public DeveloperDetailDto editDeveloper(String memberId, EditDeveloper.Request request) {
        validateEditDeveloperRequest(request, memberId);

        Developer developer = developerRepository.findByMemberId(memberId)
                .orElseThrow(
                        () -> new DevmakerException(NO_DEVELOPER)
                );

        developer.setDeveloperLevel(request.getDeveloperLevel());
        developer.setDeveloperSkillType(request.getDeveloperSkillType());
        developer.setExperienceYears(request.getExperienceYears());

        return DeveloperDetailDto.fromEntity(developer);
    }

    @Transactional
    public DeveloperDetailDto deleteDeveloper(String memberId) {

        // Transactional
        // 1. EMPLOYED -> RETIRED

        Developer developer = developerRepository.findByMemberId(memberId)
                .orElseThrow(
                        () -> new DevmakerException(NO_DEVELOPER)
                );

        developer.setStatusCode(StatusCode.RETIRED);
        // 2. save into RetiredDeveloper

        RetiredDeveloper retiredDeveloper = RetiredDeveloper.builder()
                .memberId(memberId)
                .name(developer.getName())
                .build();

        retiredDeveloperRepository.save(developer);

        return DeveloperDetailDto.fromEntity(developer);

    }

    private void validateCreateDeveloperRequest(CreateDeveloper.Request request) {

        validateDeveloperLevel(request.getDeveloperLevel(), request.getExperienceYears());

        developerRepository.findByMemberId(request.getMemberId()).ifPresent(developer -> {
            throw new DevmakerException(DUPLICATED_MEMBER_ID);
        });

    }

    private void validateEditDeveloperRequest(EditDeveloper.Request request, String memberId) {

        validateDeveloperLevel(request.getDeveloperLevel(), request.getExperienceYears());


    }

    private static void validateDeveloperLevel(DeveloperLevel developerLevel, Integer experienceYears) {
        if (developerLevel == DeveloperLevel.SENIOR && experienceYears < 10) {
            // custom Exception 만들기
            throw new DevmakerException(LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
        }

        if (developerLevel == DeveloperLevel.JUNGNIOR && (experienceYears < 4 || experienceYears > 10)) {
            throw new DevmakerException(LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
        }

        if (developerLevel == DeveloperLevel.JUNIOR && experienceYears > 4) {
            throw new DevmakerException(LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
        }
    }


}
