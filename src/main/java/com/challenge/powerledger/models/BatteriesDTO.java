package com.challenge.powerledger.models;

import lombok.*;

import javax.persistence.Column;
import javax.validation.constraints.*;

@Value
public class BatteriesDTO {

    @NotBlank
    @Size(min = 2, max = 32, message = "name must be between 2 and 32 characters long")
    @Column(name = "name")
    String name;

    @NotNull
    @Min(value = 200, message = "postcode must be between 200 & 9999")
    @Max(value = 9999, message = "postcode must be between 200 & 9999")
    @Column(name = "postcode")
    int postcode;

    @NotNull
    @Column(name = "watt_capacity")
    int wattCapacity;
}
