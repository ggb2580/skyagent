package com.hrbu.aidemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hrbu.aidemo.dto.FlightDTO;
import com.hrbu.aidemo.entity.Flight;
import com.hrbu.aidemo.mapper.FlightMapper;
import com.hrbu.aidemo.service.IFlightService;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class FlightServiceImpl extends ServiceImpl<FlightMapper, Flight> implements IFlightService {
@Autowired
FlightMapper flightMapper;
    /*
    * 查询航班
    * */
    @Override
    public List<Flight> queryFlight(FlightDTO flightDTO) {
        QueryWrapper<Flight> queryWrapper = new QueryWrapper<>();
        LocalDate date = flightDTO.getDepartureDate();
        //开始时间
        LocalDateTime start = date.atStartOfDay();
        //结束时间
        LocalDateTime end = date.plusDays(1).atStartOfDay();

        queryWrapper.eq("departure",flightDTO.getDeparture());
        queryWrapper.eq("destination",flightDTO.getDestination());

        //时间范围
        queryWrapper.ge("departure_time",start);
        queryWrapper.lt("departure_time",end);

        return flightMapper.selectList(queryWrapper);
    }

}
