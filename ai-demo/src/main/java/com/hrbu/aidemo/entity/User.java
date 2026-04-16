package com.hrbu.aidemo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("user")
public class User {
    /*
    * 用户Id
    * */
    private Long id;

    /*
    * 姓名
    * */
    private String name;

    /*
    * 身份证
    * */
    private String idCard;

    /*
    * 护照号
    * */
    private String passportNum;

    /*
    * 手机号
    * */
    private String phoneNum;

    /*
    * 邮箱
    * */
    private String email;

}
