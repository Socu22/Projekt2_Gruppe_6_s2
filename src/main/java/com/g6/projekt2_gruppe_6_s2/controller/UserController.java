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
    UserRepositoryDatabase userRepo; // repository connecting to database

    @GetMapping("/Login") // action when user requests to login/register page
    public String login(HttpServletRequest request, Model model)
    {
        // if user is already in a valid session, ie logged in, direct to their profile page instead
        if (getSession(request, model) != null) return "redirect:/Profile";
        return "login";
    }

    @GetMapping("/Logout") // action when user requests to logout
    public String logout(HttpServletRequest request)
    {
        // action should not be available if valid session does not exist,
        // still, calling 'getSession' without false argument, ensures 'invalidate' is not called on 'null'
        request.getSession().invalidate(); // invalidates the user's session.
        return "index"; // send user to homepage, without 'redirect/Home' as session does assuredly not exist
    }

    @PostMapping("/VerifyLogin") // action when user submits credentials to login
    public String verify(@RequestParam("username")  String username,
                         @RequestParam("password")  String password,
                         HttpServletRequest request,
                         Model model)
    {
        try
        {
            User user = userRepo.getUser(username, password); // fetch corresponding user from database

            if (user != null) // TODO: redundant 'if'? repo throws anyway in the case no such user exists or password is wrong
            {
                setSession(user, request, model); // registers user in a valid session

                // print log message behind the scenes
                System.out.println(LocalDateTime.now() + "\u001B[32m  LOGN\u001B[35m user\u001B[0m ---- [" + username + "]");

                return "redirect:/Profile"; // redirect user to their profile page
            }
            model.addAttribute("registerMsg", "An unknown Error has occurred."); // should NEVER happen
        }
        catch (SQLException|IllegalArgumentException e)
        {
            // catches potential exceptions from repo, these should be formulated fittingly to display for user.
            // print log message behind the scenes
            System.out.println(LocalDateTime.now() + "\u001B[32m  LOGN\u001B[35m fail\u001B[0m ---- [" + username + "]");
            model.addAttribute("loginMsg", e.getMessage()); // display fault to user
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

            if (delete(username, password, model)) // remove user from database if already exists and correct passwords
            {
                System.out.println(LocalDateTime.now() + "\u001B[32m  DELT\u001B[35m user\u001B[0m ---- [" + username + "]");
                return "login"; // returns user to login page
            }

            User user = userRepo.addUser(username, password); // adds user to database if not already exists and correct passwords

            // login user
            if (user != null) // TODO: redundant 'if'? repo throws anyway in the case no such user exists or password is wrong
            {
                setSession(user, request, model);

                System.out.println(LocalDateTime.now() + "\u001B[32m  RGST\u001B[35m user\u001B[0m ---- [" + username + "]");

                return "redirect:/Profile";
            }
            model.addAttribute("registerMsg", "An unknown Error has occurred."); // should NEVER happen
        }
        catch (SQLException|IllegalArgumentException e)
        {
            // catches potential exceptions from repo/non-matching passwords,
            // these should be formulated fittingly to display for user.
            // print log message behind the scenes
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

    private void setSession(User user, HttpServletRequest request, Model model)
    {
        HttpSession session = request.getSession();

        session.setMaxInactiveInterval(1200);
        session.setAttribute("activeUser", user);
        model.addAttribute("isLoggedIn",true);
    }

    public static HttpSession getSession(HttpServletRequest request, Model model)
    {
        HttpSession session = request.getSession(false);
        model.addAttribute("isLoggedIn",session != null);
        if(session != null) model.addAttribute("activeUser",session.getAttribute("activeUser"));

        return session;
    }
}
