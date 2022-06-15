package com.innovator.innovator.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Products {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private EType type;

    @OneToMany(mappedBy = "products", cascade = CascadeType.ALL)
    private List<Blocks> blocks;

    public Products(EType type) {
        this.type = type;
    }
}
