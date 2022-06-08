package com.test.powerledger.models;

import lombok.Value;

import java.util.List;

@Value
public class BatteriesListDTO {

    List<String> names;
    int totalWattCapacity;
    double averageWattCapacity;
}
