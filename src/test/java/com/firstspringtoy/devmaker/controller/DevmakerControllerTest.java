package com.firstspringtoy.devmaker.controller;

import com.firstspringtoy.devmaker.dto.DeveloperDto;
import com.firstspringtoy.devmaker.service.DevmakerService;
import com.firstspringtoy.devmaker.type.DeveloperLevel;
import com.firstspringtoy.devmaker.type.DeveloperSkillType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(DevmakerController.class)
class DevmakerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DevmakerService devmakerService;

    protected MediaType contentType = new MediaType(
            MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getType(),
            StandardCharsets.UTF_8
    );

    @Test
    void getAllDevelopers() throws Exception {
        DeveloperDto juniorDeveloperDto = DeveloperDto.builder()
                .developerSkillType(DeveloperSkillType.BACK_END)
                .developerLevel(DeveloperLevel.JUNIOR)
                .memberId("memberId1").build();

        DeveloperDto seniorDeveloperDto = DeveloperDto.builder()
                .developerSkillType(DeveloperSkillType.FRONT_END)
                .developerLevel(DeveloperLevel.SENIOR)
                .memberId("memberId1")
                .build();

        given(devmakerService.getAllEmployedDevelopers())
                .willReturn(
                        Arrays.asList(juniorDeveloperDto, seniorDeveloperDto)
                );

        mockMvc.perform(get("/developers")
                        .contentType(contentType))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath(
                        "$.[0].developerSkillType",
                        is(DeveloperSkillType.BACK_END.name()))
                )
                .andExpect(jsonPath(
                        "$.[0].developerLevel",
                        is(DeveloperLevel.JUNIOR.name()))
                )
                .andExpect(jsonPath(
                        "$.[1].developerSkillType",
                        is(DeveloperSkillType.FRONT_END.name()))
                )
                .andExpect(jsonPath(
                        "$.[1].developerLevel",
                        is(DeveloperLevel.SENIOR.name()))
                );


    }
}