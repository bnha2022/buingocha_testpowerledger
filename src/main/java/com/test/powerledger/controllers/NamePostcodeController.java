package com.test.powerledger.controllers;


import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.test.powerledger.models.BatteriesDTO;
import com.test.powerledger.models.BatteriesListDTO;
import com.test.powerledger.services.NameBatteriesService;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@Validated
@RequiredArgsConstructor
@Log4j2
public class NamePostcodeController {

    @NonNull
    private final NameBatteriesService nameBatteriesService;

    @PostMapping(value = "/name")
    public ResponseEntity<Void> storeListOfPostcodeNames(@RequestBody List<@Valid BatteriesDTO> postcodeDTOs) {
        logger.debug("Received request to store postcode ranges: {}", () -> postcodeDTOs);
        nameBatteriesService.saveListOfNamePostcodeDTOs(postcodeDTOs);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/name")
    public ResponseEntity<BatteriesListDTO> fetchNamesListByPostcodeRange(
            @RequestParam(value = "postcodeStart")
            @Min(value = 200, message = "postcode must be between 200 & 9999")
            @Max(value = 9999, message = "postcode must be between 200 & 9999") int postcodeStart,
            @RequestParam(value = "postcodeEnd")
            @Min(value = 200, message = "postcode must be between 200 & 9999")
            @Max(value = 9999, message = "postcode must be between 200 & 9999") int postcodeEnd) {
        logger.debug("Received request to fetch names by postcodes from {} to {}", () -> postcodeStart, () -> postcodeEnd);
        var responseBody = nameBatteriesService.fetchBatteriesListDTOByPostcodeRange(postcodeStart, postcodeEnd);
        logger.trace("Response result {} for postcodes from {} to {}", () -> responseBody, () -> postcodeStart, () -> postcodeEnd);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }
}
