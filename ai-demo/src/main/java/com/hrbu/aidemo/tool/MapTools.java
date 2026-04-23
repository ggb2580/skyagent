package com.hrbu.aidemo.tool;

import com.hrbu.aidemo.util.GaoDeRouteService;
import com.hrbu.aidemo.util.RouteRequest;
import com.hrbu.aidemo.util.RouteResult;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import static com.baomidou.mybatisplus.extension.ddl.DdlScriptErrorHandler.PrintlnLogErrorHandler.log;

/**
 * @author Say my name
 */
@Component(value = "mapTools")
public class MapTools {

    private static final String AMAP_API_KEY = System.getenv("AMAP_API_KEY");
    private static final Logger log = LoggerFactory.getLogger(MapTools.class);
    private final RestTemplate restTemplate = new RestTemplate();


    @Autowired
    private GaoDeRouteService gaoDeRouteService;

    @Tool(name = "getRouteByDriving", value = "获取两地之间驾车方式出行的相关信息，据起点、终点和出行方式规划路线，返回距离、耗时，mode参数：驾车=driving，骑行=bicycling，步行=walking")
    public RouteResult getRouteByDriving(@P("起点") String origin,@P("终点")String destination,@P("你需要传入出行方式mode") String mode){
        log.info("===================");
        log.info("起点："+origin);
        log.info("终点："+destination);
        log.info("===================");
        System.out.println("出行方式："+mode);
        RouteRequest request = new RouteRequest();
        request.setMode(mode);
        request.setOrigin(gaoDeRouteService.getGeo(origin));
        request.setDestination(gaoDeRouteService.getGeo(destination));
        return gaoDeRouteService.getDrivingRoute(request);
    }

////    @Tool("根据起点、终点和出行方式规划路线，返回距离、耗时。mode参数：驾车=driving，骑行=bicycling，步行=walking")
//    public String planRoute(@P("起点") String origin, @P("终点") String destination, @P("出行方式") String mode) {
//        try {
//            // 1. 地理编码（使用稳定的 v3 接口）
//            String originLocation = geocode(origin);
//            String destLocation = geocode(destination);
//
//            // 2. 校验 mode
//            if (!isValidMode(mode)) {
//                return "不支持的出行方式，请选择：driving(驾车)、bicycling(骑行)、walking(步行)";
//            }
//
//            // 3. 根据 mode 选择不同的 API 地址
//            String url;
//            if ("bicycling".equals(mode)) {
//                url = String.format(
//                        "https://restapi.amap.com/v4/direction/bicycling?origin=%s&destination=%s&key=%s&output=json",
//                        originLocation, destLocation, AMAP_API_KEY
//                );
//            } else {
//                url = String.format(
//                        "https://restapi.amap.com/v3/direction/%s?origin=%s&destination=%s&key=%s&output=json",
//                        mode, originLocation, destLocation, AMAP_API_KEY
//                );
//            }
//
//            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
//            return parseRouteResult(response, mode);
//
//        } catch (Exception e) {
//            return "路线规划失败：" + e.getMessage();
//        }
//    }
//
//    // 地理编码使用 v3 接口
//    private String geocode(String address) {
//        // 1. 先尝试 POI 搜索，确保坐标在道路上
//        String poiUrl = String.format(
//                "https://restapi.amap.com/v3/place/text?keywords=%s&city=%s&types=&key=%s&output=json&offset=1&page=1",
//                address, address, AMAP_API_KEY
//        );
//        try {
//            Map poiResponse = restTemplate.getForObject(poiUrl, Map.class);
//            Object poisObj = poiResponse.get("pois");
//            if (poisObj instanceof List) {
//                List pois = (List) poisObj;
//                if (pois != null && !pois.isEmpty()) {
//                    String location = (String) ((Map) pois.get(0)).get("location");
//                    System.out.println("POI搜索成功: " + address + " -> " + location);
//                    return location;
//                }
//            }
//        } catch (Exception e) {
//            System.out.println("POI搜索失败，回退地理编码: " + e.getMessage());
//        }
//
//        // 2. 回退：普通地理编码（v3）
//        String geoUrl = String.format(
//                "https://restapi.amap.com/v3/geocode/geo?address=%s&key=%s&city=%s",
//                address, AMAP_API_KEY, address
//        );
//        Map geoResponse = restTemplate.getForObject(geoUrl, Map.class);
//        List geocodes = (List) geoResponse.get("geocodes");
//        if (geocodes == null || geocodes.isEmpty()) {
//            throw new RuntimeException("地址解析失败：" + address);
//        }
//        return (String) ((Map) geocodes.get(0)).get("location");
//    }
//
//    private boolean isValidMode(String mode) {
//        return "driving".equals(mode) || "bicycling".equals(mode) || "walking".equals(mode);
//    }
//
//    private String parseRouteResult(Map<String, Object> response, String mode) {
//        // 检查 API 状态
//        if (!"1".equals(response.get("status").toString())) {
//            return "API调用失败：" + response.get("info");
//        }
//
//        String modeName = switch (mode) {
//            case "driving" -> "驾车";
//            case "bicycling" -> "骑行";
//            case "walking" -> "步行";
//            default -> mode;
//        };
//
//        // 骑行：解析 v4 格式（data.paths）
//        if ("bicycling".equals(mode)) {
//            Map data = (Map) response.get("data");
//            if (data == null || !data.containsKey("paths")) {
//                return "骑行路线规划失败：返回数据异常，可能该路线不支持骑行（如距离过长、无非机动车道等）";
//            }
//            List paths = (List) data.get("paths");
//            if (paths == null || paths.isEmpty()) {
//                return "未找到合适的骑行路线，可能起终点距离过远或道路条件不允许骑行";
//            }
//            Map firstPath = (Map) paths.get(0);
//            int distance = Integer.parseInt(firstPath.get("distance").toString());
//            int duration = Integer.parseInt(firstPath.get("duration").toString());
//
//            return String.format(
//                    "{\"mode\":\"%s\",\"distance_km\":%.1f,\"duration_min\":%d}",
//                    modeName, distance / 1000.0, duration / 60
//            );
//        }
//        // 驾车 / 步行：解析 v3 格式（route.paths）
//        else {
//            Map routeObj = (Map) response.get("route");
//            if (routeObj == null || !routeObj.containsKey("paths")) {
//                return "未找到合适的路线，请检查起点和终点是否正确";
//            }
//            List paths = (List) routeObj.get("paths");
//            if (paths == null || paths.isEmpty()) {
//                return "未找到合适的路线，请检查起点和终点是否正确";
//            }
//            Map firstPath = (Map) paths.get(0);
//            int distance = Integer.parseInt(firstPath.get("distance").toString());
//            int duration = Integer.parseInt(firstPath.get("duration").toString());
//
//            return String.format(
//                    "{\"mode\":\"%s\",\"distance_km\":%.1f,\"duration_min\":%d}",
//                    modeName, distance / 1000.0, duration / 60
//            );
//        }
//    }
}