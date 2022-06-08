package com.challenge.powerledger.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ExceptionDTO {

    private final String message;
    private final String error;
}
