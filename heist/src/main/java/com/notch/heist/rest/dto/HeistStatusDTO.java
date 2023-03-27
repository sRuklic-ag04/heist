package com.notch.heist.rest.dto;

import com.notch.heist.domain.enums.HeistStatus;

public class HeistStatusDTO {

    private HeistStatus status;

    public HeistStatus getStatus() {
        return status;
    }

    public void setStatus(HeistStatus status) {
        this.status = status;
    }
}
