package com.innovator.innovator.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Articles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String pictureUrl;

    @Column(length = 1000000)
    private String description;

    @JsonIgnore
    @Lob
    private byte[] picture;

    @JsonIgnore
    private String pictureName;

}
