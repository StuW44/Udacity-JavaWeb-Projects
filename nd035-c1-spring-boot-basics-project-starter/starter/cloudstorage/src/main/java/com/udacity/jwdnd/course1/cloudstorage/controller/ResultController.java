package com.udacity.jwdnd.course1.cloudstorage.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping
public class ResultController {

    @GetMapping("/result")
    public String homeView(Authentication authentication, HttpSession session, Model model)
    {
        if(session.getAttribute("saveFailure")!=null) {
            model.addAttribute("saveFailure", true);
        }
        else if(session.getAttribute("saveSuccess")!=null) {
            model.addAttribute("saveSuccess", true);
        }
        else if(session.getAttribute("generalFailure")!=null)
        {
            model.addAttribute("generalFailure", true);
            model.addAttribute("generalFailureMessage", session.getAttribute("generalFailureMessage"));
        }
        session.setAttribute("saveFailure", null);
        session.setAttribute("saveSuccess", null);
        session.setAttribute("generalFailure",null);
        session.setAttribute("generalFailureMessage",null);
        return "result";
    }
}
