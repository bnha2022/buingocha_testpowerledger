package com.challenge.powerledger.controllers;


import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import com.challenge.powerledger.models.BatteriesDTO;
import com.challenge.powerledger.models.BatteriesListDTO;
import com.challenge.powerledger.services.BatteriesService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BatteriesController.class)
@ContextConfiguration
public class BatteriesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BatteriesService batteriesService;

    @Captor
    private ArgumentCaptor<List<BatteriesDTO>> namePostcodeDTOListCaptor;

    @Test
    public void shouldStoreEmptyArray() throws Exception {
        MockHttpServletRequestBuilder request = post("/batteries")
                .contentType(MediaType.APPLICATION_JSON)
                .content("[]");

        this.mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isCreated());

        verify(batteriesService, times(1)).saveListOfNamePostcodeDTOs(namePostcodeDTOListCaptor.capture());
        var capturedInput = namePostcodeDTOListCaptor.getValue();
        assertThat(capturedInput).isEmpty();
    }

    @Test
    public void shouldStoreArrayOfBatteries() throws Exception {
        MockHttpServletRequestBuilder request = post("/batteries")
                .contentType(MediaType.APPLICATION_JSON)
                .content("[{ \"batteryName\": \"Anker\", \"postcode\": 1000, \"wattCapacity\": 150 }, { \"batteryName\": \"Energizer\", \"postcode\": 9999, \"wattCapacity\": 150 }]");

        this.mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isCreated());

        verify(batteriesService, times(1)).saveListOfNamePostcodeDTOs(namePostcodeDTOListCaptor.capture());
        var capturedInput = namePostcodeDTOListCaptor.getValue();
        assertThat(capturedInput).hasSize(2)
                .extracting(BatteriesDTO::getBatteryName).containsExactly("Anker", "Energizer");
        assertThat(capturedInput)
                .extracting(BatteriesDTO::getPostcode).containsExactly(1000, 9999);
        assertThat(capturedInput)
                .extracting(BatteriesDTO::getWattCapacity).containsExactly(150,150);
    }

    @Test
    public void shouldErrorWithMissingName() throws Exception {
        MockHttpServletRequestBuilder request = post("/batteries")
                .contentType(MediaType.APPLICATION_JSON)
                .content("[{ \"postcode\": 6000 }]");

        this.mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("must not be blank")));

        verify(batteriesService, times(0)).saveListOfNamePostcodeDTOs(anyList());
    }

    @Test
    public void shouldErrorWithNullName() throws Exception {
        MockHttpServletRequestBuilder request = post("/batteries")
                .contentType(MediaType.APPLICATION_JSON)
                .content("[{ \"batteryName\": null, \"postcode\": 6000, \"wattCapacity\": 150 }]");

        this.mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("must not be blank")));

        verify(batteriesService, times(0)).saveListOfNamePostcodeDTOs(anyList());
    }

    @Test
    public void shouldErrorWithEmptyName() throws Exception {
        MockHttpServletRequestBuilder request = post("/batteries")
                .contentType(MediaType.APPLICATION_JSON)
                .content("[{ \"batteryName\": \"\", \"postcode\": 6000,  \"wattCapacity\": 6000 }]");

        this.mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("must not be blank")));

        verify(batteriesService, times(0)).saveListOfNamePostcodeDTOs(anyList());
    }

    @Test
    public void shouldErrorWithTooShortName() throws Exception {
        MockHttpServletRequestBuilder request = post("/batteries")
                .contentType(MediaType.APPLICATION_JSON)
                .content("[{ \"batteryName\": \"c\", \"postcode\": 6000,  \"wattCapacity\": 150 }]");

        this.mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("name must be between 2 and 32 characters long")));

        verify(batteriesService, times(0)).saveListOfNamePostcodeDTOs(anyList());
    }

    @Test
    public void shouldErrorWithTooLongName() throws Exception {
        MockHttpServletRequestBuilder request = post("/batteries")
                .contentType(MediaType.APPLICATION_JSON)
                .content("[{ \"batteryName\": \"dhfjrnghtidjskwifoentorowlsmcjfod\", \"postcode\": 6000 }]");

        this.mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("name must be between 2 and 32 characters long")));

        verify(batteriesService, times(0)).saveListOfNamePostcodeDTOs(anyList());
    }

    @Test
    public void shouldErrorWithMissingPostcode() throws Exception {
        MockHttpServletRequestBuilder request = post("/batteries")
                .contentType(MediaType.APPLICATION_JSON)
                .content("[{ \"batteryName\": \"Anker\" }]");

        this.mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("postcode must be between 200 & 9999")));

        verify(batteriesService, times(0)).saveListOfNamePostcodeDTOs(anyList());
    }

    @Test
    public void shouldErrorWithPostcodeBelowRange() throws Exception {
        MockHttpServletRequestBuilder request = post("/batteries")
                .contentType(MediaType.APPLICATION_JSON)
                .content("[{ \"batteryName\": \"Anker\", \"postcode\": 199, \"wattCapacity\": 150 }]");

        this.mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("postcode must be between 200 & 9999")));

        verify(batteriesService, times(0)).saveListOfNamePostcodeDTOs(anyList());
    }

    @Test
    public void shouldErrorWithPostcodeAboveRange() throws Exception {
        MockHttpServletRequestBuilder request = post("/batteries")
                .contentType(MediaType.APPLICATION_JSON)
                .content("[{ \"batteryName\": \"Anker\", \"postcode\": 10000 }]");

        this.mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("postcode must be between 200 & 9999")));

        verify(batteriesService, times(0)).saveListOfNamePostcodeDTOs(anyList());
    }

    @Test
    public void shouldErrorWithInvalidPostcodeType() throws Exception {
        MockHttpServletRequestBuilder request = post("/batteries")
                .contentType(MediaType.APPLICATION_JSON)
                .content("[{ \"batteryName\": \"Anker\", \"postcode\": \"Cheeky\" }]");

        this.mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("JSON parse error: Cannot deserialize value of type `int`")));

        verify(batteriesService, times(0)).saveListOfNamePostcodeDTOs(anyList());
    }

    @Test
    public void shouldFetchWithEmptyResponse() throws Exception {
        doReturn(new BatteriesListDTO(List.of(), 0, 0.0))
                .when(batteriesService)
                .fetchBatteriesListDTOByPostcodeRange(eq(201), eq(9998));

        MockHttpServletRequestBuilder request = get("/batteries?postcodeStart=201&postcodeEnd=9998")
                .contentType(MediaType.APPLICATION_JSON);

        this.mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("{\"names\":[],\"totalWattCapacity\":0,\"averageWattCapacity\":0.0}")));

        verify(batteriesService, times(1)).fetchBatteriesListDTOByPostcodeRange(201, 9998);
    }

    @Test
    public void shouldErrorWithLowerBoundStart() throws Exception {
        MockHttpServletRequestBuilder request = get("/batteries?postcodeStart=199&postcodeEnd=9999")
                .contentType(MediaType.APPLICATION_JSON);

        this.mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("postcode must be between 200 & 9999")));
    }

    @Test
    public void shouldErrorWithLUpperBoundStart() throws Exception {
        MockHttpServletRequestBuilder request = get("/batteries?postcodeStart=10000&postcodeEnd=9999")
                .contentType(MediaType.APPLICATION_JSON);

        this.mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("postcode must be between 200 & 9999")));
    }

    @Test
    public void shouldErrorWithLowerBoundEnd() throws Exception {
        MockHttpServletRequestBuilder request = get("/batteries?postcodeStart=200&postcodeEnd=199")
                .contentType(MediaType.APPLICATION_JSON);

        this.mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("postcode must be between 200 & 9999")));
    }

    @Test
    public void shouldErrorWithLUpperBoundEnd() throws Exception {
        MockHttpServletRequestBuilder request = get("/batteries?postcodeStart=200&postcodeEnd=10000")
                .contentType(MediaType.APPLICATION_JSON);

        this.mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("postcode must be between 200 & 9999")));
    }


}
