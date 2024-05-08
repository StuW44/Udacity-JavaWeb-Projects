package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping
public class CredentialController {

    private  final UserService  userService;
    private final CredentialService credentialService;
    private final NoteService noteService;
    private final FileService fileService;
    public CredentialController(FileService fileService, UserService userService, CredentialService credentialService, NoteService noteService)
    {
        this.userService=userService;
        this.credentialService=credentialService;
        this.noteService=noteService;
        this.fileService=fileService;
    }
   @PostMapping("/credential")
   public String addCredential( HttpSession session,
                                Authentication authentication,
                                @RequestParam("credentialId") Integer credentialId,
                                @RequestParam("url") String url,
                                @RequestParam("username") String username,
                                @RequestParam("password") String password,
                                Model model)
   {

       try
       {
           User userDb = userService.getUser(authentication.getName());

           if(credentialId==null || credentialId==0) {
               credentialService.insertCredential(url,username, this.credentialService.generateKey(), password,userDb.getUserId());}
           else {
               credentialService.editCredential(credentialId,url,username,  password,userDb.getUserId());}
               model.addAttribute("notes", this.noteService.getUserNotes(userDb.getUserId()));
               model.addAttribute("credentials",this.credentialService.getUserCredentials(userDb.getUserId()));
               model.addAttribute("files",this.fileService.getUserFiles(userDb.getUserId()));
               session.setAttribute("saveSuccess", true);
       }
       catch (Exception ex) {
                session.setAttribute("saveFailure", true);
       }

       return "redirect:/result";
   }
    @GetMapping("/deleteCredential/{credentialid}")
    public String deleteNote(HttpSession session,Authentication auth, @PathVariable(value = "credentialid") Integer credentialid, Model model) {
        try {
            this.credentialService.deleteCredential(credentialid);
            User userDb = userService.getUser(auth.getName());
            model.addAttribute("notes", this.noteService.getUserNotes(userDb.getUserId()));
            model.addAttribute("credentials", this.credentialService.getUserCredentials(userDb.getUserId()));
            model.addAttribute("files", this.fileService.getUserFiles(userDb.getUserId()));
            session.setAttribute("saveSuccess", true);
        }
        catch (Exception ex) {
            session.setAttribute("saveFailure", true);
        }

        return "redirect:/result";
    }
}
