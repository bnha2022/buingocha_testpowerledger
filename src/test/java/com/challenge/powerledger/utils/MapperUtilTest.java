package com.challenge.powerledger.utils;



import org.junit.jupiter.api.Test;
import com.challenge.powerledger.models.BatteriesDTO;

import static org.assertj.core.api.Assertions.assertThat;

public class MapperUtilTest {

    @Test
    public void shouldMappedToEntityCorrectly() {
        var dto = new BatteriesDTO("Xiaomi", 6158, 150);
        var entity = MapperUtil.toEntity(dto);

        assertThat(entity.getName()).isEqualTo("Xiaomi");
        assertThat(entity.getPostcode()).isEqualTo(6158);
        assertThat(entity.getWattCapacity()).isEqualTo(150);
        assertThat(entity.getId()).isNull();
    }
}
