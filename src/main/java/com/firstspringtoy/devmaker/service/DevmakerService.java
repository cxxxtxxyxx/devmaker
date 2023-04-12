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
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.firstspringtoy.devmaker.code.StatusCode.EMPLOYED;
import static com.firstspringtoy.devmaker.exception.DevmakerErrorCode.DUPLICATED_MEMBER_ID;
import static com.firstspringtoy.devmaker.exception.DevmakerErrorCode.NO_DEVELOPER;

@Service
@RequiredArgsConstructor
public class DevmakerService {
    private final DeveloperRepository developerRepository;
    private final RetiredDeveloperRepository retiredDeveloperRepository;

    @Transactional
    public CreateDeveloper.Response createDeveloper(CreateDeveloper.Request request) {

        validateCreateDeveloperRequest(request);


        return CreateDeveloper.Response.fromEntity(
                developerRepository.save(
                        createDeveloperFromRequest(request)
                )
        );
    }

    private Developer createDeveloperFromRequest(@NonNull CreateDeveloper.Request request) {
        return Developer.builder()
                .developerLevel(request.getDeveloperLevel())
                .developerSkillType(request.getDeveloperSkillType())
                .experienceYears(request.getExperienceYears())
                .memberId(request.getMemberId())
                .name(request.getName())
                .age(request.getAge())
                .statusCode(EMPLOYED)
                .build();
    }


    @Transactional(readOnly = true)
    public List<DeveloperDto> getAllEmployedDevelopers() {
        return developerRepository.findDeveloperByStatusCodeEquals(EMPLOYED)
                .stream()
                .map(DeveloperDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DeveloperDetailDto getDeveloperDetail(String memberId) {
        return DeveloperDetailDto.fromEntity(getDeveloperByMemberId(memberId));
    }

    @Transactional
    public DeveloperDetailDto editDeveloper(String memberId, EditDeveloper.Request request) {
        validateEditDeveloperRequest(request, memberId);


        return DeveloperDetailDto.fromEntity(
                getUpdatedDeveloperFromRequest(
                        request, getDeveloperByMemberId(memberId)
                )
        );
    }

    private Developer getUpdatedDeveloperFromRequest(
            EditDeveloper.Request request, Developer developer
    ) {
        developer.setDeveloperLevel(request.getDeveloperLevel());
        developer.setDeveloperSkillType(request.getDeveloperSkillType());
        developer.setExperienceYears(request.getExperienceYears());

        return developer;
    }

    private Developer getDeveloperByMemberId(String memberId) {
        return developerRepository.findByMemberId(memberId)
                .orElseThrow(
                        () -> new DevmakerException(NO_DEVELOPER)
                );
    }

    @Transactional
    public DeveloperDetailDto deleteDeveloper(String memberId) {

        // Transactional
        // 1. EMPLOYED -> RETIRED

        Developer developer = getDeveloperByMemberId(memberId);

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

        request.getDeveloperLevel().validateExperienceYears(
                request.getExperienceYears()
        );

        developerRepository.findByMemberId(request.getMemberId()).ifPresent(developer -> {
            throw new DevmakerException(DUPLICATED_MEMBER_ID);
        });

    }

    private void validateEditDeveloperRequest(EditDeveloper.Request request, String memberId) {

        request.getDeveloperLevel().validateExperienceYears(
                request.getExperienceYears()
        );


    }

}
