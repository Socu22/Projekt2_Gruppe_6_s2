package com.g6.projekt2_gruppe_6_s2.controller;

import com.g6.projekt2_gruppe_6_s2.model.User;
import com.g6.projekt2_gruppe_6_s2.repository.UserRepositoryDatabase;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.SQLException;
import java.time.LocalDateTime;

@Controller
public class UserController
{
    @Autowired
    UserRepositoryDatabase userRepo;

    @GetMapping("/Login")
    public String login()
    {
        return "login";
    }

    @PostMapping("/VerifyLogin")
    public String verify(@RequestParam("username") String username,
                         @RequestParam("password") String password,
                         HttpServletRequest request,
                         Model model)
    {
        model.addAttribute("loginMsg", "");

        try
        {
            User user = userRepo.getUser(username, password);

            if (user != null)
            {
                HttpSession session = request.getSession();

                session.setAttribute("activeUser", user);

                System.out.println(LocalDateTime.now() + "\u001B[32m  LOGN\u001B[35m user\u001B[0m ---- [" + username + "]");

                return "redirect:/Profile";
            }
            System.out.println(LocalDateTime.now() + "\u001B[32m  LOGN\u001B[35m fail\u001B[0m ---- [" + username + "]");
        }
        catch (SQLException|IllegalArgumentException e)
        {
            model.addAttribute("loginMsg", e.getMessage());
        }

        return "login"; // Return to the login page with an error message
    }

    @PostMapping("/Register")
    public String register(@RequestParam("username") String username,
                           @RequestParam("password") String password,
                           HttpServletRequest request,
                           Model model)
    {
        model.addAttribute("registerMsg", "");

        try
        {
            User user = userRepo.addUser(username, password);

            if (user != null)
            {
                HttpSession session = request.getSession();

                session.setAttribute("activeUser", user);

                System.out.println(LocalDateTime.now() + "\u001B[32m  RGST\u001B[35m user\u001B[0m ---- [" + username + "]");

                return "redirect:/Profile";
            }
        }
        catch (SQLException|IllegalArgumentException e)
        {
            model.addAttribute("registerMsg", e.getMessage());
        }

        return "login"; // Return to the login page with an error message
    }
}
