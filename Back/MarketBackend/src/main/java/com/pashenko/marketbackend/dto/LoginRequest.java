package com.pashenko.marketbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class LoginRequest {
    private String username;
    private String password;
}
