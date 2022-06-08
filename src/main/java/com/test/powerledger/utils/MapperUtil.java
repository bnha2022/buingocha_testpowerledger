package com.test.powerledger.utils;


import com.test.powerledger.models.BatteriesDTO;
import com.test.powerledger.models.Battery;

public class MapperUtil {

    public static Battery toEntity(BatteriesDTO dto) {
        return Battery.builder()
                .batteryName(dto.getBatteryName())
                .postcode(dto.getPostcode())
                .wattCapacity(dto.getWattCapacity())
                .build();
    }
}
