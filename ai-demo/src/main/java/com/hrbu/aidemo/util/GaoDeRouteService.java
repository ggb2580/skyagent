package com.hrbu.aidemo.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * @author Say my name
 */
@Service
@RequiredArgsConstructor
public class GaoDeRouteService {

    private final GaoDeConfig config;
    private final RestTemplate restTemplate = new RestTemplate();

    /*
    * 获取路线
    * */
    public RouteResult getDrivingRoute(RouteRequest request) {
        if ("bicycling".equals(request.getMode())){
            config.setDrivingUrl("https://restapi.amap.com/v4/direction/");
        }
        String url = UriComponentsBuilder.fromHttpUrl(config.getDrivingUrl()+request.getMode())
                .queryParam("origin", request.getOrigin())
                .queryParam("destination", request.getDestination())
                .queryParam("key", config.getKey())
                .queryParam("strategy", 0) // 推荐策略
                .toUriString();

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(response.getBody());
                JsonNode route = root.path("route");
                JsonNode paths = route.path("paths");
                if (paths.isArray() && paths.size() > 0) {
                    JsonNode path = paths.get(0);
                    return new RouteResult(
                            path.path("distance").asText(),
                            path.path("duration").asText(),
                            path.path("strategy").asText()
                    );
                }
            } catch (Exception e) {
                throw new RuntimeException("解析高德路线规划响应失败", e);
            }
        }

        throw new RuntimeException("调用高德地图接口失败");
    }

    /*
     * 地理名转GEO
     * */
    public String getGeo(String address) {
        // 1. 构建请求（Spring 自动编码中文，绝对不会乱码）
        String url = UriComponentsBuilder.fromHttpUrl("https://restapi.amap.com/v3/geocode/geo")
                .queryParam("key", config.getKey())
                .queryParam("address", address)
                .queryParam("output", "JSON")
                .build()
                .toUriString();

        // 2. 发请求
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        // ==========================================
        // 关键调试：把高德返回的真实内容打印出来！
        // ==========================================
        String body = response.getBody();
        System.out.println("==================== 高德返回 ====================");
        System.out.println(body);
        System.out.println("==================================================");

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(body);

            // 读取结果
            JsonNode geocodes = root.path("geocodes");

            // 地址不存在时返回提示，不抛异常
            if (geocodes == null || geocodes.isEmpty()) {
                System.out.println("⚠️ 地址未匹配：" + address);
                return "未匹配到地址";
            }

            // 取第一个
            JsonNode first = geocodes.get(0);
            String location = first.path("location").asText();
            System.out.println("✅ 获取经纬度成功：" + location);

            return location;

        } catch (Exception e) {
            throw new RuntimeException("解析失败，返回内容：" + body, e);
        }
    }

}