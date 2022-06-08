package com.challenge.powerledger.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "battery",
        uniqueConstraints = @UniqueConstraint(name = "uniqueNameAndPostcode", columnNames = {"name", "postcode", "wattCapacity"}
))
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Battery {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable = false)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer postcode;

    @Column(nullable = false)
    private Integer wattCapacity;


}
