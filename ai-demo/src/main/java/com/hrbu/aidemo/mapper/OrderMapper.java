package com.hrbu.aidemo.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hrbu.aidemo.entity.Order;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {
}
