package com.chatApp.ChatApplication.controller;

import com.chatApp.ChatApplication.services.SendEMailImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Health {
    @Autowired
    SendEMailImpl sendEMail;

    @GetMapping("/health")
    public String Check() {
        return "working ";
    }

}

