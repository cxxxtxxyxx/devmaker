package com.firstspringtoy.devmaker.service;

import com.firstspringtoy.devmaker.code.StatusCode;
import com.firstspringtoy.devmaker.dto.CreateDeveloper;
import com.firstspringtoy.devmaker.dto.DeveloperDetailDto;
import com.firstspringtoy.devmaker.entity.Developer;
import com.firstspringtoy.devmaker.exception.DevmakerErrorCode;
import com.firstspringtoy.devmaker.exception.DevmakerException;
import com.firstspringtoy.devmaker.repository.DeveloperRepository;
import com.firstspringtoy.devmaker.repository.RetiredDeveloperRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.firstspringtoy.devmaker.type.DeveloperLevel.SENIOR;
import static com.firstspringtoy.devmaker.type.DeveloperSkillType.FRONT_END;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    private final CreateDeveloper.Request defaultCreateRequest = CreateDeveloper.Request.builder()
            .developerLevel(SENIOR)
            .developerSkillType(FRONT_END)
            .memberId("memberId")
            .experienceYears(12)
            .name("name")
            .age(32)
            .build();

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

        ArgumentCaptor<Developer> captor = ArgumentCaptor.forClass(Developer.class);


        //when
        CreateDeveloper.Response developer = devmakerService.createDeveloper(defaultCreateRequest);

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
        DevmakerException devmakerException = assertThrows(DevmakerException.class, () -> devmakerService.createDeveloper(defaultCreateRequest));

        assertEquals(DevmakerErrorCode.DUPLICATED_MEMBER_ID, devmakerException.getDevmakerErrorcode());

    }
}