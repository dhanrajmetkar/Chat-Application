package com.chatApp.ChatApplication.model;

import lombok.*;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Component
public class JwtRequest {
    private String email;
    private String password;
}
