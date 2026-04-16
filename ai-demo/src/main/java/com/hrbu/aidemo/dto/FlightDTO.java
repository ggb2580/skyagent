package com.hrbu.aidemo.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class FlightDTO {
    /*
    * 出发日期
    * */
    private LocalDate departureDate;

    /*
    * 出发地
    * */
    private String departure;

    /*
    * 目的地
    * */
    private String destination;
}
