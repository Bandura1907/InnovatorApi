package com.innovator.innovator.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Products {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Lob
    @JsonIgnore
    private byte[] photo;

    @JsonIgnore
    private String photoName;

    @Enumerated(EnumType.STRING)
    private EType type;

    @OneToMany(mappedBy = "products", cascade = CascadeType.ALL)
    private List<Blocks> blocks;

    public Products(EType type) {
        this.type = type;
    }
}
