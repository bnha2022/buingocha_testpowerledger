package com.challenge.powerledger.controllers;


import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.challenge.powerledger.models.BatteriesDTO;
import com.challenge.powerledger.models.BatteriesListDTO;
import com.challenge.powerledger.services.BatteriesService;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

/**
 * The type Batteries controller.
 */
@RestController
@Validated
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/batteries")
public class BatteriesController {

    @NonNull
    private final BatteriesService batteriesService;

    /**
     * Store list of postcode names response entity.
     *
     * @param postcodeDTOs the postcode dt os
     * @return the response entity
     */
    @PostMapping
    public ResponseEntity<Void> storeListOfPostcodeNames(@RequestBody List<@Valid BatteriesDTO> postcodeDTOs) {
        logger.debug("Received request to store postcode ranges: {}", () -> postcodeDTOs);
        batteriesService.saveListOfNamePostcodeDTOs(postcodeDTOs);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Fetch names list by postcode range response entity.
     *
     * @param postcodeStart the postcode start
     * @param postcodeEnd   the postcode end
     * @return the response entity
     */
    @GetMapping
    public ResponseEntity<BatteriesListDTO> fetchNamesListByPostcodeRange(
            @RequestParam(value = "postcodeStart")
            @Min(value = 200, message = "postcode must be between 200 & 9999")
            @Max(value = 9999, message = "postcode must be between 200 & 9999") int postcodeStart,
            @RequestParam(value = "postcodeEnd")
            @Min(value = 200, message = "postcode must be between 200 & 9999")
            @Max(value = 9999, message = "postcode must be between 200 & 9999") int postcodeEnd) {
        logger.debug("Received request to fetch names by postcodes from {} to {}", () -> postcodeStart, () -> postcodeEnd);
        var responseBody = batteriesService.fetchBatteriesListDTOByPostcodeRange(postcodeStart, postcodeEnd);
        logger.trace("Response result {} for postcodes from {} to {}", () -> responseBody, () -> postcodeStart, () -> postcodeEnd);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }
}
