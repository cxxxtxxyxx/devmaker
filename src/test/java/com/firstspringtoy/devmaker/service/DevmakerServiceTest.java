package com.firstspringtoy.devmaker.service;

import com.firstspringtoy.devmaker.code.StatusCode;
import com.firstspringtoy.devmaker.dto.CreateDeveloper;
import com.firstspringtoy.devmaker.dto.DeveloperDetailDto;
import com.firstspringtoy.devmaker.entity.Developer;
import com.firstspringtoy.devmaker.exception.DevmakerErrorCode;
import com.firstspringtoy.devmaker.exception.DevmakerException;
import com.firstspringtoy.devmaker.repository.DeveloperRepository;
import com.firstspringtoy.devmaker.repository.RetiredDeveloperRepository;
import com.firstspringtoy.devmaker.type.DeveloperLevel;
import com.firstspringtoy.devmaker.type.DeveloperSkillType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.firstspringtoy.devmaker.constant.DevmakerConstant.MAX_JUNIOR_EXPERIENCE_YEARS;
import static com.firstspringtoy.devmaker.constant.DevmakerConstant.MIN_SENIOR_EXPERIENCE_YEARS;
import static com.firstspringtoy.devmaker.type.DeveloperLevel.*;
import static com.firstspringtoy.devmaker.type.DeveloperSkillType.FRONT_END;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


//@SpringBootTest
@ExtendWith(MockitoExtension.class)
class DevmakerServiceTest {

    // 두개의 repo를 가상의 mock으로 DevmakerServiceTest 안에 등록하게됨
    // DevmakerService 생성시 자동으로 해당 mock들을 넣어줌
    @Mock
    private DeveloperRepository developerRepository;

    @Mock
    private RetiredDeveloperRepository retiredDeveloperRepository;


    @InjectMocks
    private DevmakerService devmakerService;


    private final Developer defaultDeveloper = Developer.builder()
            .developerLevel(SENIOR)
            .developerSkillType(FRONT_END)
            .experienceYears(12)
            .statusCode(StatusCode.EMPLOYED)
            .name("name")
            .age(12)
            .build();

    private CreateDeveloper.Request getCreateRequest(
            DeveloperLevel developerLevel,
            DeveloperSkillType developerSkillType,
            Integer experienceYears
    ) {

        return CreateDeveloper.Request.builder()
                .developerLevel(developerLevel)
                .developerSkillType(developerSkillType)
                .memberId("memberId")
                .experienceYears(experienceYears)
                .name("name")
                .age(32)
                .build();
    }

    @Test
    public void getDeveloperDetailTest() {


        //given
        given(developerRepository.findByMemberId(anyString()))
                .willReturn(Optional.of(defaultDeveloper));

        //when
        DeveloperDetailDto developerDetail = devmakerService.getDeveloperDetail("memberId");


        //then
        assertEquals(SENIOR, developerDetail.getDeveloperLevel());
        assertEquals(FRONT_END, developerDetail.getDeveloperSkillType());
        assertEquals(12, developerDetail.getAge());

    }

    @Test
    void createDeveloperTest_success() {
        //given


        given(developerRepository.findByMemberId(anyString()))
                .willReturn(Optional.empty());
        given(developerRepository.save(any()))
                .willReturn(defaultDeveloper);

        ArgumentCaptor<Developer> captor = ArgumentCaptor.forClass(Developer.class);


        //when
        devmakerService.createDeveloper(getCreateRequest(SENIOR, FRONT_END, 12));

        //then
        verify(developerRepository, times(1))
                .save(captor.capture());

        Developer savedDeveloper = captor.getValue();

        assertEquals(SENIOR, savedDeveloper.getDeveloperLevel());
        assertEquals(FRONT_END, savedDeveloper.getDeveloperSkillType());
        assertEquals(12, savedDeveloper.getExperienceYears());
    }

    @Test
    void createDeveloperTest_failed_with_duplicated() {
        //given

        given(developerRepository.findByMemberId(anyString()))
                .willReturn(Optional.of(defaultDeveloper));

        //when
        //then
        DevmakerException devmakerException = assertThrows(DevmakerException.class, () -> devmakerService.createDeveloper(getCreateRequest(SENIOR, FRONT_END, 12)));

        assertEquals(DevmakerErrorCode.DUPLICATED_MEMBER_ID, devmakerException.getDevmakerErrorcode());

    }

    @Test
    void createDeveloperTest_fail_unmatched_level() {
        //given


        //when
        //then
        DevmakerException devmakerException = assertThrows(DevmakerException.class, () -> devmakerService.createDeveloper(
                        getCreateRequest(
                                JUNIOR, FRONT_END, MAX_JUNIOR_EXPERIENCE_YEARS + 1)
                )
        );

        assertEquals(DevmakerErrorCode.LEVEL_EXPERIENCE_YEARS_NOT_MATCHED, devmakerException.getDevmakerErrorcode());

        devmakerException = assertThrows(DevmakerException.class, () -> devmakerService.createDeveloper(
                        getCreateRequest(
                                JUNGNIOR, FRONT_END, MIN_SENIOR_EXPERIENCE_YEARS + 1)
                )
        );


        assertEquals(DevmakerErrorCode.LEVEL_EXPERIENCE_YEARS_NOT_MATCHED, devmakerException.getDevmakerErrorcode());

        devmakerException = assertThrows(DevmakerException.class, () -> devmakerService.createDeveloper(
                        getCreateRequest(
                                SENIOR, FRONT_END, MIN_SENIOR_EXPERIENCE_YEARS - 1)
                )
        );


        assertEquals(DevmakerErrorCode.LEVEL_EXPERIENCE_YEARS_NOT_MATCHED, devmakerException.getDevmakerErrorcode());

    }
}