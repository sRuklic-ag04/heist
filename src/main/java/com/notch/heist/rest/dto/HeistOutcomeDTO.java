package com.notch.heist.rest.dto;

import com.notch.heist.domain.enums.HeistStatus;

public class HeistOutcomeDTO {

    private HeistStatus outcome;

    public HeistStatus getOutcome() {
        return outcome;
    }

    public void setOutcome(HeistStatus outcome) {
        this.outcome = outcome;
    }
}
