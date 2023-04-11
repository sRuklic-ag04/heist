package com.notch.heist.service.util;

import org.springframework.stereotype.Component;

@Component
public class Util {

    public double calculatePercentage(int actual, int total) {
        return ((double) actual / (double) total) * 100.0;
    }
}
