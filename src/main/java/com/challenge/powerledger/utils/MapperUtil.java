package com.challenge.powerledger.utils;


import com.challenge.powerledger.models.BatteriesDTO;
import com.challenge.powerledger.entity.Battery;

public class MapperUtil {

    public static Battery toEntity(BatteriesDTO dto) {
        return Battery.builder()
                .name(dto.getName())
                .postcode(dto.getPostcode())
                .wattCapacity(dto.getWattCapacity())
                .build();
    }
}
