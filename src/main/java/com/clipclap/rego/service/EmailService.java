package com.clipclap.rego.service;

public interface EmailService {
    String sendSimpleMessage(String to)throws Exception;

    void sendEmail(String toEmail, String subject, String message);
}