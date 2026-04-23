package com.realestate.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Controller
@RequestMapping("/owner")
public class UploadController {

    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file) {

        try {
            File folder = new File("uploads");
            if (!folder.exists()) {
                folder.mkdirs();
            }

            String path = "uploads/" + file.getOriginalFilename();
            file.transferTo(new File(path));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/owner/dashboard";
    }
}