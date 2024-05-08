package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Controller
@RequestMapping
public class HomeController {

    private final UserService userService;
    private final NoteService noteService;
    private final CredentialService credentialService;
    private final FileService fileService;
    public HomeController(FileService fileService, UserService userService, CredentialService credentialService, NoteService noteService)
    {
        this.fileService=fileService;
        this.userService = userService;
        this.credentialService = credentialService;
        this.noteService = noteService;
    }
    @GetMapping("/home")
    public String homeView(Authentication authentication,  Model model)
    {

        if(!authentication.isAuthenticated())
        {
            return "login";
        }
        else {
            User userDb = userService.getUser(authentication.getName());
            model.addAttribute("notes", this.noteService.getUserNotes(userDb.getUserId()));
            model.addAttribute("credentials", this.credentialService.getUserCredentials(userDb.getUserId()));
            model.addAttribute("files",this.fileService.getUserFiles(userDb.getUserId()));
            return "home";
        }
    }

    @GetMapping("/home/killsession")
    public String logoutView(HttpServletRequest request, HttpServletResponse response){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login";
    }



}