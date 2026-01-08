package com.example.rideshare.controller.v2;

import com.example.rideshare.dto.EmailRequestDTO;
import com.example.rideshare.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {
    @Autowired
    private EmailService emailService;

    @PostMapping("/email")
    public String sendEmail(@RequestBody EmailRequestDTO email){
        emailService.sendMail(email.getTo(), email.getSubject(), email.getText());
        return "Email sent";
    }

}
