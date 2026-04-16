package com.hrbu.aidemo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hrbu.aidemo.dto.FlightDTO;
import com.hrbu.aidemo.entity.Flight;
import com.hrbu.aidemo.mapper.FlightMapper;

import java.util.List;

public interface IFlightService extends IService<Flight> {
    List<Flight> queryFlight(FlightDTO flightDTO);

}
