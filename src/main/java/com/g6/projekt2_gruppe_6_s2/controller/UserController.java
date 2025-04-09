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
        model.addAttribute("loginError", false);
        model.addAttribute("loginMsg", "");

        try
        {
            User user = userRepo.getUser(username, password);

            if (user != null)
            {
                HttpSession session = request.getSession();

                model.addAttribute("activeUser", user);
                session.setAttribute("activeUser", user);

                model.addAttribute("loginError", false);
                model.addAttribute("loginMsg", "");

                return "redirect:/Profile";
            }
        }
        catch (SQLException|IllegalArgumentException e)
        {
            model.addAttribute("loginError", true);
            model.addAttribute("loginMsg", e.getMessage());
        }

        return "login"; // Return to the login page with an error message
    }

    public String register(@RequestParam("username") String username,
                           @RequestParam("password") String password,
                           HttpServletRequest request,
                           Model model)
    {
        model.addAttribute("registerMsg", "");

        try
        {
            User user = userRepo.getUser(username, password);

            if (user == null)
            {

                user = userRepo.getUser(username, password);

                HttpSession session = request.getSession();

                model.addAttribute("activeUser", user);
                session.setAttribute("activeUser", user);

                model.addAttribute("loginError", false);
                model.addAttribute("loginMsg", "");

                return "redirect:/Profile";
            }
        }
        catch (SQLException|IllegalArgumentException e)
        {
            model.addAttribute("loginError", true);
            model.addAttribute("loginMsg", e.getMessage());
        }

        return "login"; // Return to the login page with an error message
    }
}
