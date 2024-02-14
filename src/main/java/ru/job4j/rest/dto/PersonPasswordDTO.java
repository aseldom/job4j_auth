package ru.job4j.rest.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class PersonPasswordDTO {

    @NotNull
    private int id;

    @Size(min = 6)
    private String password;

}
