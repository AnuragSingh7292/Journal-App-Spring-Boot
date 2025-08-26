package com.example.JournalApp.controller;

import com.example.JournalApp.JournalEntities.Users;
import com.example.JournalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
//@RequestMapping("/Journal")
public class PageController {
    @Autowired
    private UserService userService;

    // localhost:8080/Journal/ OR /Journal/home
    @GetMapping({"/", "/home","/index"})
    public String homePage() {
        return "index"; // loads templates/index.html
    }

    // localhost:8080/Journal/login
    @GetMapping("/login")
    public String loginPage() {
        return "login"; // loads templates/login.html
    }

    // localhost:8080/Journal/signup
    @GetMapping("/signup")
    public String registerPage() {
        return "signup"; // loads templates/register.html
    }

    // localhost:8080/Journal/resetPass
    @GetMapping({"/resetPass", "/resetpassword", "/resetPassword", "/reset-password"})
    public String resetPassPage() {
        return "resetPass"; // loads templates/resetPass.html
    }

    // localhost:8080/Journal/signup
    @GetMapping({"/editProfile", "/editprofile", "/edit-profile"})
    public String editProfilePage(Model model, Authentication authentication) {
        // Check authentication
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            return "redirect:/login";
        }

        // Get logged-in user
        String username = authentication.getName();
        Users user = userService.findByUsername(username);

        if (user != null) {
            // Ensure avatar has a default fallback
            String avatarUrl = user.getAvatarUrl();
            if (avatarUrl == null || avatarUrl.isEmpty()) {
                avatarUrl = "/images/avatar.jpg";
            } else if (!avatarUrl.startsWith("/")) {
                avatarUrl = "/" + avatarUrl;
            }
            user.setAvatarUrl(avatarUrl);

            // Add user object to model
            model.addAttribute("user", user);
        }

        return "editProfile"; // Load editProfile.html
    }



    @GetMapping({"/dashboard", "/Dashboard"})
    public String dashboardPage(Model model, Authentication authentication) {
        // If user is not authenticated, redirect to login
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            return "redirect:/login";
        }

        String username = authentication.getName();
        Users user = userService.findByUsername(username);

        if (user != null) {
            // Add user details to model
            model.addAttribute("user", user);

            // Set default avatar if missing
            String avatarUrl = user.getAvatarUrl();
            if (avatarUrl == null || avatarUrl.isEmpty()) {
                avatarUrl = "/images/avatar.jpg";
            } else if (!avatarUrl.startsWith("/")) {
                avatarUrl = "/" + avatarUrl;
            }
            user.setAvatarUrl(avatarUrl);
        }
        return "dashboard"; // Load dashboard.html
    }

    @GetMapping({"/profile", "/Profile", "prof"})
    public String profilePage(Model model, Authentication authentication) {
        // If user is not authenticated, redirect to login
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            return "redirect:/login";
        }
        String username = authentication.getName();
        Users user = userService.findByUsername(username);

        if (user != null) {
            // Add user details to model
            model.addAttribute("user", user);

            // Set default avatar if missing
            if (user.getAvatarUrl() == null || user.getAvatarUrl().isEmpty()) {
                user.setAvatarUrl("/Journal/images/avatar.jpg");
            }
        }
        return "profile"; // Load dashboard.html
    }


//    @GetMapping({"/setting", "/Setting", "sett"})
//    public String settingPage(Model model, Authentication authentication) {
//        // If user is not authenticated, redirect to login
//        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
//            return "redirect:/login";
//        }
//        String username = authentication.getName();
//        Users user = userService.findByUsername(username);
//
//        if (user != null) {
//            // Add user details to model
//            model.addAttribute("user", user);
//
//            // Set default avatar if missing
//            if (user.getAvatarUrl() == null || user.getAvatarUrl().isEmpty()) {
//                user.setAvatarUrl("/Journal/images/avatar.jpg");
//            }
//    }
//        return "setting"; // Load dashboard.html
//    }
}
