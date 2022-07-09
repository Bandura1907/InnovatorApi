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
public class Videos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String pictureUrl;
    private String videoUrl;

    @JsonIgnore
    private String pictureName;

    @JsonIgnore
    @Lob
    private byte[] picture;

//    @JsonIgnore
//    @ManyToOne(fetch = FetchType.EAGER)
//    private Useful useful;
}
