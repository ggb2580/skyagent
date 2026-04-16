package com.hrbu.aidemo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Say my name
 */
@Data
@TableName("flight")
public class Flight {
    /*
    * 航班id
    * */
    private Long id;

    /*
    * 航班号
    * */
    private String flightNum;

    /*
    * 出发地
    * */
    private String departure;

    /*
    * 目的地
    * */
    private String destination;

    /*
    * 出发时间
    * */
    private LocalDateTime departureTime;

    /*
    * 到达时间
    * */
    private LocalDateTime destinationTime;

    /*
    * 航空公司
    * */
    private String airline;

    /*
    * 票价
    * */
    private BigDecimal ticketPrice;

    /*
    * 飞机机型
    * */
    private String aircraftType;

    /*
    * 航空公司Log URL
    * */
    private String airlineLog;

}
