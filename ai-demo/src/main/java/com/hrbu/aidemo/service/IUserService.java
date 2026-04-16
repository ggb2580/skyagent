package com.hrbu.aidemo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hrbu.aidemo.entity.User;


public interface IUserService extends IService<User> {
    boolean createUserInfo(User user);
}
