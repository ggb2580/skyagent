package com.hrbu.aidemo.service;


import java.util.List;
import java.util.Map;

/**
 * @author Say my name
 */
public interface IReadOnlyQueryService {
    public List<Map<String, Object>> executeQuery(String originalSql);
}
