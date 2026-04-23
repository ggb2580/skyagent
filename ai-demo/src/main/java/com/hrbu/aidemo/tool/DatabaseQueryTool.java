package com.hrbu.aidemo.tool;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hrbu.aidemo.exception.UnsafeSqlException;
import com.hrbu.aidemo.service.IReadOnlyQueryService;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author Say my name
 */
@Component("databaseQueryTool")
public class DatabaseQueryTool {
    private static final Logger log = LoggerFactory.getLogger(DatabaseQueryTool.class);
    @Autowired
    private IReadOnlyQueryService readOnlyQueryService;
    @Autowired
    private ObjectMapper objectMapper;

    @Tool("执行只读 SQL 查询，返回JSON格式的结果集，只能使用 SELECT 语句")
    public String queryDatabase(@P("合法的 SQL SELECT 语句") String sql){
        try {
            log.info("=======sql执行：======="+sql);
            List<Map<String, Object>> maps = readOnlyQueryService.executeQuery(sql);
            //结果集转成 JSON 字符串
            return objectMapper.writeValueAsString(maps);
        } catch (JsonProcessingException e) {
           return "{\"error\":\"" + e.getMessage() +"\"}";
        }catch (Exception e){
            return "{\"error\": \"查询执行失败："+ e.getMessage() + "\"}";
        }
    }
}
