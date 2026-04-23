package com.hrbu.aidemo.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteRequest {
    private String origin;      // 起点坐标 - 格式：经度,纬度
    private String destination; // 终点坐标 - 格式：经度,纬度
    private String mode; //出行方式 骑行 驾车 徒步

}
