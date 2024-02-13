package ru.job4j.rest.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class PersonDTO {

    @NotNull
    private int id;

    @NotBlank
    private String login;

    @Size(min = 6)
    private String password;

}
