package com.hrbu.aidemo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("order")
public class Order {
    /*
    * 订单ID
    * */
    private Long id;
    /*
    * 用户ID
    * */
    private Long userId;

    /*
    * 航班号
    * */
    private String flightNum;

    /*
    * 出行日期
    * */
    private LocalDate date;

    /*
    * 机票价格
    * */
    private BigDecimal ticketPrice;

    /*
    * 机场建设税费
    * */
    private BigDecimal airportFees;

    /*
    * 燃油税费
    * */
    private BigDecimal fuelTax;

    /*
    * 保险费
    * */
    private BigDecimal insurance;

    /*
    * 附加服务费
    * */
    private BigDecimal additionalServiceCharge;

    /*
    * 实际支付金额
    * */
    private BigDecimal actualPayment;

    /*
    * 支付状态（0-取消  1-未付款  2-已付款  3-申请退款  4-已退款 5-已完成 ）
    * */
    private Integer paymentStatus;

    /*
    * 支付时间
    * */
    private LocalDateTime paymentTime;

    /*
    * 交易完成时间
    * */
    private LocalDateTime transactionCompletionTime;

    /*
    * 创建时间
    * */
    private LocalDateTime createTime;

    /*
    * 更新时间
    * */
    private LocalDateTime updateTime;
}
