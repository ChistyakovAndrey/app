package com.spandev.app.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserCredDTO {

    private final String username;

    private final String password;

}
