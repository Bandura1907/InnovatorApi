package com.innovator.innovator.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.Instant;

@Entity
@Getter @Setter
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private Instant createAt;

    private String username;

    private String description;

    public Activity() {
        this.createAt = Instant.now();
    }

    public Activity(String username, String description) {
        this.createAt = Instant.now();
        this.username = username;
        this.description = description;
    }
}
