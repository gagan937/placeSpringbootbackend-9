package com.example.demo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService {

    @Value("${resend.api.key}")
    private String apiKey;

    @Value("${resend.from.email}")
    private String fromEmail;

    private static final String RESEND_API_URL = "https://api.resend.com/emails";

    public void sendVerificationEmail(String toEmail, String link) {
        send(toEmail, "Verify your email",
                "<p>Click the link to verify your account:</p><a href=\"" + link + "\">" + link + "</a>");
    }

    public void sendOtpEmail(String toEmail, String otp) {
        send(toEmail, "Your Password Reset OTP",
                "<p>Aapka OTP hai: <b>" + otp + "</b></p><p>Ye 10 minute me expire ho jayega.</p>");
    }

    private void send(String toEmail, String subject, String htmlBody) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        Map<String, Object> body = new HashMap<>();
        body.put("from", fromEmail);
        body.put("to", new String[]{toEmail});
        body.put("subject", subject);
        body.put("html", htmlBody);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        restTemplate.postForEntity(RESEND_API_URL, request, String.class);
    }
}