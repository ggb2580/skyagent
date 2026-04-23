package com.hrbu.aidemo.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteResult {
    private String distance;  // 距离（单位：米）
    private String duration;  // 时间（单位：秒）
    private String strategy;  // 策略说明

}
