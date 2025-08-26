package com.example.JournalApp.Scheduler;


import com.example.JournalApp.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


@Component
public class UserWelcome {

    @Autowired
    private EmailService emailService;

    @Async
    public void  findUserAndSendSAMail(String userName, String email)
    {
        try{
//            String subject = "🎉 Welcome to MindScribe – We're Glad You're Here!";
//
//            String body = "Hi " + userName + ",\n\n" +
//                    "Welcome to **MindScribe** – your personal space to reflect, grow, and thrive.\n\n" +
//                    "We're excited to have you on board! Whether you're tracking your thoughts, managing moods, or reflecting on your journey – you're in the right place.\n\n" +
//                    "Feel free to explore and make the most of your experience. If you have any questions or feedback, we're always here to help.\n\n" +
//                    "Warm regards,\n" +
//                    "Team MindScribe";
//            emailService.sendEmail(email,subject,body);


            String subject = "🎉 Welcome to MindScribe – We're Glad You're Here!";

            String htmlBody = "<div style=\"font-family: Arial, sans-serif; font-size: 16px; color: #333; line-height: 1.6;\">" +
                    "<p>Hi <strong>" + userName + "</strong>,</p>" +

                    "<p><strong>Welcome to <a href='http://localhost:8080/Journal/' style='color:#4A90E2; text-decoration: none;'>MindScribe</a></strong> – your personal space to reflect, grow, and thrive.</p>" +

                    "<p>We're excited to have you on board! Whether you're tracking your thoughts, managing moods, or reflecting on your journey – you're in the right place.</p>" +

                    "<p>Feel free to explore and make the most of your experience. If you have any questions or feedback, we're always here to help.</p>" +

                    "<p style='margin-top: 30px;'>Warm regards,<br>" +
                    "<strong>Team MindScribe</strong></p>" +
                    "</div>";

            emailService.sendHtmlEmail(email, subject, htmlBody);
        } catch (Exception e){
            System.out.println(e);
        }
    }

}

