package com.g6.projekt2_gruppe_6_s2.controller;

import com.g6.projekt2_gruppe_6_s2.model.User;
import com.g6.projekt2_gruppe_6_s2.repository.UserRepositoryDatabase;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
    UserRepositoryDatabase repoUser;

    @GetMapping("/Login")
    public String login(Model model)
    {
        model.addAttribute("loginMsg", "Hello");
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
            User user = repoUser.getUserWithUsernamePassword(username, password);

            if (user != null)
            {
                HttpSession session = request.getSession();

                model.addAttribute("activeUser", user);
                session.setAttribute("userId", user.getId());

                System.out.println("Works");

                return "myprofile";
            }
        }
        catch (SQLException e)
        {
            model.addAttribute("loginError", true);
            model.addAttribute("loginMsg", e.getMessage());
        }

        return "login"; // Return to the login page with an error message
    }

    private void success(User user, HttpSession session)
    {

    }

    private void faliure(User user)
    {

    }

}
