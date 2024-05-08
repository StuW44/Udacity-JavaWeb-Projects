package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.udacity.jwdnd.course1.cloudstorage.model.File;

import javax.servlet.http.HttpSession;
import java.io.*;

@Controller
@RequestMapping
public class FileController {
    private final UserService userService;
    private final CredentialService credentialService;
    private final NoteService noteService;
    private final FileService fileService;
    public FileController(FileService fileService,UserService userService, CredentialService credentialService, NoteService noteService)
    {
        this.userService = userService;
        this.credentialService = credentialService;
        this.noteService = noteService;
        this.fileService=fileService;
    }
    @PostMapping("/file/file-upload")
    public String doFileUpload(Authentication authentication,HttpSession session,@RequestParam("fileUpload") MultipartFile fileUpload, Model model)
    {
            try {
                User userDb = userService.getUser(authentication.getName());
                if(fileService.doesFileExist(fileUpload.getOriginalFilename(),userDb.getUserId())) {
                    session.setAttribute("generalFailure",true);
                    session.setAttribute("generalFailureMessage","File already exists!!!");
                }
                else {
                    File file = new File(fileUpload.getOriginalFilename(),
                            fileUpload.getContentType(),
                            (int) fileUpload.getSize(),
                            userDb.getUserId(),
                            fileUpload.getBytes());
                    fileService.Insert(file);
                    model.addAttribute("notes", this.noteService.getUserNotes(userDb.getUserId()));
                    model.addAttribute("credentials", this.credentialService.getUserCredentials(userDb.getUserId()));
                    model.addAttribute("files", this.fileService.getUserFiles(userDb.getUserId()));
                    session.setAttribute("saveSuccess", true);
                }
            }
            catch (Exception exception)
            {
                session.setAttribute("saveFailure", true);
            }
        return "redirect:/result";
    }
    @GetMapping("/deleteFile/{fileId}")
    public String deleteFile(Authentication auth, HttpSession session,@PathVariable(value = "fileId") Integer fileId, Model model)
    {
        try
        {
            this.fileService.deleteFile(fileId);
            User userDb = userService.getUser(auth.getName());
            model.addAttribute("notes", this.noteService.getUserNotes(userDb.getUserId()));
            model.addAttribute("credentials",this.credentialService.getUserCredentials(userDb.getUserId()));
            model.addAttribute("files",this.fileService.getUserFiles(userDb.getUserId()));
            session.setAttribute("saveSuccess", true);
        }
        catch (Exception exception)
        {
            session.setAttribute("saveFailure", true);
        }
        return "redirect:/result";
    }
    @GetMapping("/getFile/{fileId}")
    public ResponseEntity<ByteArrayResource> getFile(Authentication auth, @PathVariable(value = "fileId") Integer fileId, Model model) {
        File file=fileService.getFile(fileId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getContenttype()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""
                + file.getFilename() + "\"").body(new ByteArrayResource(file.getFiledata()));
    }
}
