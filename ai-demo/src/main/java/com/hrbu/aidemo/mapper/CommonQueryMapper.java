package com.hrbu.aidemo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hrbu.aidemo.entity.Flight;
import com.hrbu.aidemo.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author Say my name
 */
@Mapper
public interface CommonQueryMapper {
    List<Map<String,Object>> executeSelect(@Param("value") String sql);
}
