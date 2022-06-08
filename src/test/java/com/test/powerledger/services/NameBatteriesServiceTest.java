package com.test.powerledger.services;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import com.test.powerledger.models.BatteriesDTO;
import com.test.powerledger.models.Battery;
import com.test.powerledger.repositories.NameBatteriesRepository;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {NameBatteriesService.class})
public class NameBatteriesServiceTest {

    @MockBean
    private NameBatteriesRepository repository;

    @Autowired
    private NameBatteriesService nameBatteriesService;

    @AfterEach
    public void teardown() {
        Mockito.reset(repository);
    }

    @Captor
    public ArgumentCaptor<List<Battery>> listArgumentCaptor;

    @Test
    public void shouldStoreEmptyList() {
        nameBatteriesService.saveListOfNamePostcodeDTOs(new ArrayList<>());
        verify(repository, times(1)).saveAll(listArgumentCaptor.capture());

        var capturedList = listArgumentCaptor.getValue();
        assertThat(capturedList).isEmpty();
    }

    @Test
    public void shouldStoreListOfNamePostcodeDTOs() {
        nameBatteriesService.saveListOfNamePostcodeDTOs(List.of(
                new BatteriesDTO("Aukey", 7899, 100),
                new BatteriesDTO("Tesla Model X", 2203, 150)));

        verify(repository, times(1)).saveAll(listArgumentCaptor.capture());

        var capturedList = listArgumentCaptor.getValue();
        assertThat(capturedList).hasSize(2)
                .extracting(Battery::getBatteryName).containsExactly("Aukey", "Tesla Model X");
        assertThat(capturedList).extracting(Battery::getPostcode).containsExactly(7899, 2203);
    }

    @Test
    public void shouldReturnEmptyNameList() {
        doReturn(List.of(
                new Battery(1, "Anker", 2000, 150),
                new Battery(2, "Aukey", 2570, 100),
                new Battery(3, "Energizer", 4000,50)))
                .when(repository)
                .findBatteryByPostcodeBetween(eq(2000), eq(4500));

        var responseList = nameBatteriesService.fetchBatteriesListDTOByPostcodeRange(2000, 4500);

        verify(repository, times(1)).findBatteryByPostcodeBetween(eq(2000), eq(4500));
        assertThat(responseList.getNames())
                .hasSize(3)
                .containsExactly("Anker", "Aukey", "Energizer");
    }

    @Test
    public void shouldReturnValueOfWattCapacity() {
        doReturn(List.of(
                new Battery(1, "Anker", 2000, 150),
                new Battery(2, "Aukey", 2570, 100),
                new Battery(3, "Energizer", 4000,50)))
                .when(repository)
                .findBatteryByPostcodeBetween(eq(2000), eq(4500));

        var responseList = nameBatteriesService.fetchBatteriesListDTOByPostcodeRange(2000, 4500);

        assertThat(responseList.getAverageWattCapacity()).isEqualTo(100.0);
        assertThat(responseList.getTotalWattCapacity()).isEqualTo(300);

    }
}
