package com.example.LibraryExercise.controllers;

import com.example.LibraryExercise.models.User;
import com.example.LibraryExercise.repositories.UserRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping(path = "/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/signup")
    public String getSignup(User user) {
        return "usersignup";
    }

    @PostMapping("/signup")
    public String postSignup(@Valid User user, BindingResult bindingResult, HttpSession session) {
        if(bindingResult.hasErrors()){
            return "usersignup";
        }
        userRepository.save(user);
        session.setAttribute("loggedUser", user);
        return "redirect:/";
    }

    @GetMapping("/login")
    public String getLogin(User user) {
        return "usersignin";
    }

    @PostMapping("/login")
    public String postLogin(@RequestParam("username") String username, @RequestParam("password") String password, HttpSession session) {
        User user = userRepository.login(username, password);
        if (user == null) {
            return "usersignin";
        } else {
            session.setAttribute("loggedUser", user);
            return "redirect:/";
        }
    }

    @PostMapping("/logout")
    public String postLogout(HttpSession session) {
        session.setAttribute("loggedUser", null);
        return "redirect:/user/login";
    }

    @GetMapping("/detail")
    public ModelAndView getUserDetail(HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            return new ModelAndView(new RedirectView("/user/login"));
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("userdetail");
        modelAndView.addObject("user", user);
        return modelAndView;
    }
}
