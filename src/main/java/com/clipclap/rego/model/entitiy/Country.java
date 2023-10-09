package com.clipclap.rego.model.entitiy;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "country")
public class Country {

    @Id
    private String countryCode;

    @Column(unique = true, nullable = false)
    private String countryName;

    private String flag;


    @OneToMany(mappedBy = "countryName", cascade = CascadeType.REMOVE)
    private List<City> citys;
}




