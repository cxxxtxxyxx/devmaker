package com.firstspringtoy.devmaker.exception;

import lombok.Getter;

@Getter
public class DevmakerException extends RuntimeException {
    private DevmakerErrorCode devmakerErrorcode;
    private String detailMessage;

    public DevmakerException(DevmakerErrorCode devmakerErrorcode) {
        super(devmakerErrorcode.getMessage());
        this.devmakerErrorcode = devmakerErrorcode;
        this.detailMessage = devmakerErrorcode.getMessage();
    }

    public DevmakerException(DevmakerErrorCode devmakerErrorcode, String detailMessage) {
        super(detailMessage);
        this.devmakerErrorcode = devmakerErrorcode;
        this.detailMessage = detailMessage;
    }

}
