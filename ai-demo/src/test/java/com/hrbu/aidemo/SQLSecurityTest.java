package com.hrbu.aidemo;

import com.hrbu.aidemo.security.SqlSecurityValidator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

/*
* 动态SQL
* */
@SpringBootTest
public class SQLSecurityTest {
    @Autowired
    private SqlSecurityValidator sqlSecurityValidator;
    @Value("${query.security.max-rows:100}")
    private int maxRows;

    //如果配置为空的话允许所有表
    @Value("${security.allowed-tables:}")
    private Set<String> allowedTables;

    @Value("${security.forbidden-keywords:}")
    private Set<String> forbiddenKeywords;
    @Test
    public void test01(){
        System.out.println("maxRows:"+maxRows);
        System.out.println("allowedTables:"+allowedTables);
        System.out.println("forbiddenKeywords:"+forbiddenKeywords);

    }

    @Test
    public void test02(){
        String sql = "SELECT * from user join order ";
        String enhancedSql = sqlSecurityValidator.validateAndEnhance(sql);
        System.out.println(enhancedSql);
    }
}
