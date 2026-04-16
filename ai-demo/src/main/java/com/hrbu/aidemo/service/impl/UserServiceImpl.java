package com.hrbu.aidemo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hrbu.aidemo.entity.User;
import com.hrbu.aidemo.mapper.UserMapper;
import com.hrbu.aidemo.service.IUserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Override
    public boolean createUserInfo(User user) {
        return save(user);
    }
}
