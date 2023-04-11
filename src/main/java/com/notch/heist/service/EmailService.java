package com.notch.heist.service;

public interface EmailService {

    void sendMessage(String to, String subject, String text);
}
