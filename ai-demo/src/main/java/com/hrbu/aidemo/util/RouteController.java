package com.hrbu.aidemo.util;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/route")
@RequiredArgsConstructor
public class RouteController {

    private final GaoDeRouteService routeService;

    @PostMapping("/driving")
    public ResponseEntity<RouteResult> getDrivingRoute(@RequestBody RouteRequest request) {
        RouteResult result = routeService.getDrivingRoute(request);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/walking")
    public ResponseEntity<RouteResult> getWalkingRoute(@RequestBody RouteRequest request){
        request.setOrigin(routeService.getGeo(request.getOrigin()));
        request.setDestination(routeService.getGeo(request.getDestination()));

        RouteResult result = routeService.getDrivingRoute(request);
        return ResponseEntity.ok(result);
    }
}