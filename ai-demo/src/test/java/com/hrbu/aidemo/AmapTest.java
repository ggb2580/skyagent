package com.hrbu.aidemo;

import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Map;

public class AmapTest {
    private static final String KEY = System.getenv("AMAP_API_KEY");  // 替换为你的真实Key

    public static void main(String[] args) {
        RestTemplate rest = new RestTemplate();
        
        // 1. 地理编码：获取东方市和三亚市的坐标
        String originAddr = "东方市";
        String destAddr = "三亚市";
        
        String originLngLat = geocode(originAddr, rest);
        String destLngLat = geocode(destAddr, rest);
        
        System.out.println("起点坐标: " + originLngLat);
        System.out.println("终点坐标: " + destLngLat);
        
        // 2. 调用驾车路径规划API
        String url = String.format(
            "https://restapi.amap.com/v3/direction/driving?origin=%s&destination=%s&key=%s&output=json",
            originLngLat, destLngLat, KEY
        );
        
        Map<String, Object> response = rest.getForObject(url, Map.class);
        System.out.println("API响应: " + response);
        
        // 3. 解析结果
        if (response != null && "1".equals(response.get("status").toString())) {
            List routes = (List) ((Map) response.get("route")).get("paths");  // 注意v3驾车返回的是 route.paths 还是 routes？检查文档
            // 实际上v3驾车返回的字段是 "routes" 还是 "route"? 多数文档显示是 "route" -> "paths"
            // 为了准确，先打印所有key
            System.out.println("响应keys: " + response.keySet());
            if (response.containsKey("route")) {
                Map route = (Map) response.get("route");
                List paths = (List) route.get("paths");
                if (paths != null && !paths.isEmpty()) {
                    Map firstPath = (Map) paths.get(0);
                    System.out.println("距离(米): " + firstPath.get("distance"));
                    System.out.println("耗时(秒): " + firstPath.get("duration"));
                } else {
                    System.out.println("未找到paths，可能无路线");
                }
            } else if (response.containsKey("routes")) {
                List routesList = (List) response.get("routes");
                if (routesList != null && !routesList.isEmpty()) {
                    Map firstRoute = (Map) routesList.get(0);
                    System.out.println("距离(米): " + firstRoute.get("distance"));
                    System.out.println("耗时(秒): " + firstRoute.get("duration"));
                } else {
                    System.out.println("routes为空");
                }
            } else {
                System.out.println("响应中没有route或routes字段");
            }
        } else {
            System.out.println("API调用失败: " + (response != null ? response.get("info") : "无响应"));
        }
    }
    
    private static String geocode(String address, RestTemplate rest) {
        // 先尝试POI搜索
        String poiUrl = String.format(
            "https://restapi.amap.com/v3/place/text?keywords=%s&city=%s&types=&key=%s&output=json&offset=1",
            address, address, KEY
        );
        try {
            Map resp = rest.getForObject(poiUrl, Map.class);
            List pois = (List) ((Map) resp.get("pois"));
            if (pois != null && !pois.isEmpty()) {
                String location = (String) ((Map) pois.get(0)).get("location");
                System.out.println("POI搜索: " + address + " -> " + location);
                return location;
            }
        } catch (Exception e) {
            System.out.println("POI搜索失败: " + e.getMessage());
        }
        
        // 回退到地理编码
        String geoUrl = String.format(
            "https://restapi.amap.com/v3/geocode/geo?address=%s&key=%s",
            address, KEY
        );
        Map resp = rest.getForObject(geoUrl, Map.class);
        List geocodes = (List) resp.get("geocodes");
        if (geocodes != null && !geocodes.isEmpty()) {
            String location = (String) ((Map) geocodes.get(0)).get("location");
            System.out.println("地理编码: " + address + " -> " + location);
            return location;
        }
        throw new RuntimeException("无法解析地址: " + address);
    }
}