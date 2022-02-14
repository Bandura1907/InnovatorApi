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
public class Useful {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    @Column(length = 1000000)
    private String imageUrl;

    @Column(length = 1000000)
    private String description;

    private String category;

    private Long datePublished = System.currentTimeMillis();

    @ManyToOne(fetch = FetchType.EAGER)
    private UserAuth user;
}
