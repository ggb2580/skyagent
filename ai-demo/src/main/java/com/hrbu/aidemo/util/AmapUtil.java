package com.hrbu.aidemo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AmapUtil {

    @Autowired
    private RestTemplate restTemplate;

    // 替换成你的高德 Key！
    private final String AMAP_KEY = System.getenv("AMAP_API_KEY");

    /**
     * IP定位
     */
    public String getIpLocation(String ip) {
        String url = "https://restapi.amap.com/v3/ip?key=" + AMAP_KEY + "&ip=" + ip;
        return restTemplate.getForObject(url, String.class);
    }

    /**
     * 地理编码（地址转经纬度）
     */
    public String geocode(String address) {
        String url = "https://restapi.amap.com/v3/geocode/geo?key=" + AMAP_KEY + "&address=" + address;
        return restTemplate.getForObject(url, String.class);
    }

    /**
     * 逆地理编码（经纬度转地址）
     */
    public String regeocode(double lng, double lat) {
        String url = "https://restapi.amap.com/v3/geocode/regeo?key=" + AMAP_KEY + "&location=" + lng + "," + lat;
        return restTemplate.getForObject(url, String.class);
    }
}