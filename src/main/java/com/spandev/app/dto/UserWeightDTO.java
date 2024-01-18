package com.spandev.app.dto;

import java.time.LocalDate;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public final class UserWeightDTO {

    private final String name;

    private final LocalDate age;

    private final String password;

    private final Character gender;

    private final String username;

    private final Double weight;

    private final String email;

}
