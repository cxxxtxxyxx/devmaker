package com.firstspringtoy.devmaker.dto;

import com.firstspringtoy.devmaker.exception.DevmakerErrorCode;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DevmakerErrorResponse {
    private DevmakerErrorCode errorCode;
    private String errorMessage;

}
