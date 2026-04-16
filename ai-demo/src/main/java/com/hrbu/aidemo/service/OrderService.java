package com.hrbu.aidemo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hrbu.aidemo.entity.Order;

public interface OrderService extends IService<Order> {
    Order findOrderByUserId(Long id);
    void createOrder(Order order);
}
