package com.hrbu.aidemo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hrbu.aidemo.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {

}
