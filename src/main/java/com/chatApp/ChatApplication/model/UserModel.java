package com.chatApp.ChatApplication.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Service
public class UserModel {
    private String firstname;
    private String lastname;
    private String password;
    private String email;
}
