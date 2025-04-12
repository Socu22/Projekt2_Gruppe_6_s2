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

    @GetMapping("/Logout")
    public String logout(HttpServletRequest request)
    {
        request.getSession(false).invalidate();
        return "index";
    }

    @PostMapping("/VerifyLogin")
    public String verify(@RequestParam("username")  String username,
                         @RequestParam("password")  String password,
                         HttpServletRequest request,
                         Model model)
    {
        try
        {
            User user = userRepo.getUser(username, password);

            if (user != null) // TODO: redundant 'if'?
            {
                HttpSession session = request.getSession();

                session.setAttribute("activeUser", user);

                System.out.println(LocalDateTime.now() + "\u001B[32m  LOGN\u001B[35m user\u001B[0m ---- [" + username + "]");

                return "redirect:/Profile";
            }
            model.addAttribute("registerMsg", "An unknown Error has occurred."); // should NEVER happen
        }
        catch (SQLException|IllegalArgumentException e)
        {
            System.out.println(LocalDateTime.now() + "\u001B[32m  LOGN\u001B[35m fail\u001B[0m ---- [" + username + "]");
            model.addAttribute("loginMsg", e.getMessage());
        }

        return "login"; // Return to the login page with an error message
    }

    @PostMapping("/Register")
    public String register(@RequestParam("username")  String username,
                           @RequestParam("password")  String password,
                           @RequestParam("password2") String password2,
                           HttpServletRequest request,
                           Model model)
    {
        try
        {
            if (!password.equals(password2)) throw new IllegalArgumentException("Passwords do not match.");

            if (delete(username, password, model))
            {
                System.out.println(LocalDateTime.now() + "\u001B[32m  DELT\u001B[35m user\u001B[0m ---- [" + username + "]");
                return "login";
            }

            User user = userRepo.addUser(username, password);

            if (user != null) // TODO: redundant 'if'?
            {
                HttpSession session = request.getSession();

                session.setAttribute("activeUser", user);

                System.out.println(LocalDateTime.now() + "\u001B[32m  RGST\u001B[35m user\u001B[0m ---- [" + username + "]");

                return "redirect:/Profile";
            }
            model.addAttribute("registerMsg", "An unknown Error has occurred."); // should NEVER happen
        }
        catch (SQLException|IllegalArgumentException e)
        {
            System.out.println(LocalDateTime.now() + "\u001B[32m  RGST\u001B[35m fail\u001B[0m ---- [" + username + "]");
            model.addAttribute("registerMsg", e.getMessage());
        }

        return "login"; // Return to the login page with an error message
    }

    private boolean delete(String username,
                           String password,
                           Model model)
            throws SQLException
    {
        User user;

        try
        {
            user = userRepo.getUser(username, password);
            userRepo.deleteUser(user, password);
            model.addAttribute("registerMsg", "User deleted successfully.");
        }
        catch (IllegalArgumentException e)
        {
            return false; // if no such user exists
        }

        return true;
    }
}
