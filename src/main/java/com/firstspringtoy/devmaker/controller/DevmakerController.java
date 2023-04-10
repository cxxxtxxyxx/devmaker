package com.firstspringtoy.devmaker.controller;


import com.firstspringtoy.devmaker.dto.CreateDeveloper;
import com.firstspringtoy.devmaker.dto.DeveloperDetailDto;
import com.firstspringtoy.devmaker.dto.DeveloperDto;
import com.firstspringtoy.devmaker.dto.EditDeveloper;
import com.firstspringtoy.devmaker.service.DevmakerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


/**
 * @RestController 해당 클래스를 RestController라는 타입의 Bean으로 등록을 함
 * @Component -> 가장 기본적인 Bean
 * @Controller는 @Component와 비슷함
 * @RestController = @Controller + @ResponseBody
 * @Slf4j -> log
 */

@Slf4j
@RequiredArgsConstructor
@RestController
public class DevmakerController {
    private final DevmakerService devmakerService;

    // Entity를 그대로 내려주는 것은 좋지 않음 -> 안티패턴
    // Dto를 통해 Entity와 응답 데이터를 분리하는 것이 좋음
    @GetMapping("/developers")
    public List<DeveloperDto> getAllDevelopers() {
        return devmakerService.getAllDevelopers();
    }

    @GetMapping("/developers/{memberId}")
    public DeveloperDetailDto getDeveloperDetail(
            @PathVariable String memberId
    ) {
        return devmakerService.getDeveloperDetail(memberId);
    }

    @PostMapping("/create-developer")
    public CreateDeveloper.Response createDeveloper(
            @Valid @RequestBody CreateDeveloper.Request request
    ) {
        System.out.println(request.getClass());
        return this.devmakerService.createDeveloper(request);
    }


    @PutMapping("/developer/{memberId}")
    public DeveloperDetailDto editDeveloper(
            @PathVariable String memberId,
            @Valid @RequestBody EditDeveloper.Request request
    ) {
        return devmakerService.editDeveloper(memberId, request);
    }
}
