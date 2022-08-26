package com.innovator.innovator.models;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Blocks {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Column(length = 200000)
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIncludeProperties(value = {
            "type"
    })
    private Products products;

    public Blocks(String name, String description, Products products) {
        this.name = name;
        this.description = description;
        this.products = products;
    }
}
