package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping
public class NoteController {
    private final UserService userService;
    private final NoteService noteService;
    private final CredentialService credentialService;
    private final FileService fileService;
    public NoteController(FileService fileService, UserService userService, NoteService noteService, CredentialService credentialService)
    {
        this.fileService=fileService;
        this.userService = userService;
        this.noteService = noteService;
        this.credentialService=credentialService;
    }
    @PostMapping("/note")
    public String addNote( Authentication authentication,
                           HttpSession session,
                           @RequestParam("noteId") Integer noteId,
                           @RequestParam("notetitle") String notetitle,
                           @RequestParam("notedescription") String notedescription, Model model)
    {
        try {
            User userDb = userService.getUser(authentication.getName());

            if (noteId == null || noteId == 0) {
                noteService.insertNote(notetitle, notedescription, userDb.getUserId());
            } else {
                noteService.editNote(noteId, notetitle, notedescription, userDb.getUserId());
            }
            model.addAttribute("notes", this.noteService.getUserNotes(userDb.getUserId()));
            session.setAttribute("saveSuccess", true);
        }
        catch (Exception ex) {
            session.setAttribute("saveFailure", true);
        }

        return "redirect:/result";
    }
    @GetMapping("/deleteNote/{noteid}")
    public String deleteNote(Authentication auth, HttpSession session,@PathVariable(value = "noteid") Integer noteid, Model model) {
        try {
            this.noteService.deleteNote(noteid);
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
