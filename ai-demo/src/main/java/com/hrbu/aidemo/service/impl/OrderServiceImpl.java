package com.hrbu.aidemo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hrbu.aidemo.entity.Order;
import com.hrbu.aidemo.mapper.OrderMapper;
import com.hrbu.aidemo.service.OrderService;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper,Order> implements OrderService {
    @Override
    public Order findOrderByUserId(Long id) {
        return null;
    }

    @Override
    public void createOrder(Order order) {

    }

}
