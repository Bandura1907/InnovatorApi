package com.innovator.innovator.models;

import lombok.Data;

import javax.validation.constraints.Email;


@Data
public class MessageEmail {

    @Email(message = "failed valid email")
    private String to;
    private String subject;
    private String text;
}
