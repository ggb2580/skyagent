package com.hrbu.aidemo.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "gaode")
@Data
public class GaoDeConfig {
    private String key;
    private String drivingUrl;


}