package com.chatApp.ChatApplication.model;

import lombok.*;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Component
public class JwtResponse {
    private String jwtToken;
    private String username;

}
