package org.example.acmc.util;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class EmailUtil {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendOtpEmail(String email, String otp) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject("Your OTP for Verification");
        mimeMessageHelper.setText("""
        <div style="font-family: Arial, sans-serif; line-height: 1.6;">
            <h2 style="color: steelblue;">Verify Your Email Address</h2>
            <p>Dear User,</p>
            <p>Thank you for registering with us. To complete the verification process, please click the link below to verify your email address within the next 1 minutes:</p>
            <p><a href="https://localhost:8081/verify-account?email=%s&otp=%s" target="_blank" style="background-color: steelblue; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px;" target="_blank">Verify Email</a></p>
            <p>If you did not request this, please ignore this email.</p>
            <p>Best regards,</p>
            <p>The AC Monitor and Controller Team</p>
        </div>
        """.formatted(email, otp), true);

        javaMailSender.send(mimeMessage);
    }
}
