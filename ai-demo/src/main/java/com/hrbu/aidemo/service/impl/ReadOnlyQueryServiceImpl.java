package com.hrbu.aidemo.service.impl;

import com.hrbu.aidemo.mapper.CommonQueryMapper;
import com.hrbu.aidemo.security.SqlSecurityValidator;
import com.hrbu.aidemo.service.IReadOnlyQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ReadOnlyQueryServiceImpl implements IReadOnlyQueryService {
    @Autowired
    private SqlSecurityValidator sqlSecurityValidator;
    @Autowired
    private CommonQueryMapper commonQueryMapper;
    @Override
    public List<Map<String, Object>> executeQuery(String originalSql) {
        //安全校验
        String sql = sqlSecurityValidator.validateAndEnhance(originalSql);

        //执行查询
        List<Map<String, Object>> maps = commonQueryMapper.executeSelect(sql);

        return maps;
    }
}
