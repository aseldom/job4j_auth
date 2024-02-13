package ru.job4j.rest.dto;

import lombok.Data;

@Data
public class PersonDTO {

    private int id;
    private String login;
    private String password;

}
