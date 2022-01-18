package com.innovator.innovator.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;
    private String subtitle;

    @Column(length = 1000000)
    private String pictureUrl;

    @Column(length = 1000000)
    private String videoUrl;

    @Column(length = 1000000000)
    private String text;

    @Column(length = 1000000)
    private String sourceUrl;

}
