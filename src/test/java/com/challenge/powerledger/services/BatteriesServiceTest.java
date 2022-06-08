package com.challenge.powerledger.services;


import com.challenge.powerledger.repositories.BatteriesRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import com.challenge.powerledger.models.BatteriesDTO;
import com.challenge.powerledger.entity.Battery;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {BatteriesService.class})
public class BatteriesServiceTest {

    @MockBean
    private BatteriesRepository repository;

    @Autowired
    private BatteriesService batteriesService;

    @AfterEach
    public void teardown() {
        Mockito.reset(repository);
    }

    @Captor
    public ArgumentCaptor<List<Battery>> listArgumentCaptor;

    @Test
    public void shouldStoreEmptyList() {
        batteriesService.saveListOfNamePostcodeDTOs(new ArrayList<>());
        verify(repository, times(1)).saveAll(listArgumentCaptor.capture());

        var capturedList = listArgumentCaptor.getValue();
        assertThat(capturedList).isEmpty();
    }

    @Test
    public void shouldStoreListOfNamePostcodeDTOs() {
        batteriesService.saveListOfNamePostcodeDTOs(List.of(
                new BatteriesDTO("Aukey", 7899, 100),
                new BatteriesDTO("Tesla Model X", 2203, 150)));

        verify(repository, times(1)).saveAll(listArgumentCaptor.capture());

        var capturedList = listArgumentCaptor.getValue();
        assertThat(capturedList).hasSize(2)
                .extracting(Battery::getName).containsExactly("Aukey", "Tesla Model X");
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

        var responseList = batteriesService.fetchBatteriesListDTOByPostcodeRange(2000, 4500);

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
                new Battery(3, "Energizer", 4000,25),
                new Battery(3, "Energizer", 4000,14)))
                .when(repository)
                .findBatteryByPostcodeBetween(eq(2000), eq(4500));

        var responseList = batteriesService.fetchBatteriesListDTOByPostcodeRange(2000, 4500);

        assertThat(responseList.getAverageWattCapacity()).isEqualTo(72.25);
        assertThat(responseList.getTotalWattCapacity()).isEqualTo(289);

    }
}
