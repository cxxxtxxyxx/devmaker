package com.firstspringtoy.devmaker.exception;

import lombok.Getter;

@Getter
public class DevmakerException extends RuntimeException {
    private DevmakerErrorcode devmakerErrorcode;
    private String detailMessage;

    public DevmakerException(DevmakerErrorcode devmakerErrorcode) {
        super(devmakerErrorcode.getMessage());
        this.devmakerErrorcode = devmakerErrorcode;
        this.detailMessage = devmakerErrorcode.getMessage();
    }

    public DevmakerException(DevmakerErrorcode devmakerErrorcode, String detailMessage) {
        super(detailMessage);
        this.devmakerErrorcode = devmakerErrorcode;
        this.detailMessage = detailMessage;
    }

}
