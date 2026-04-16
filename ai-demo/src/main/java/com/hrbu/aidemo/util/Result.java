package com.hrbu.aidemo.util;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Statement;

/**
 * @author Say my name
 */
@Data
@AllArgsConstructor
public class Result {
    private Integer code;
    private String message;
    private Object data;

    /*
    * 发送成功
    * */
    public static Result ok(){
        return new Result(200,"success",null);
    }
    public static Result ok(Object data){
        return new Result(200,"success",data);
    }

    /*
    * 发送失败
    * */
    public static Result fail(String message){
        return new Result(401,message,null);
    }

    public static Result fail(){
        return new Result(401,"error",null);
    }
}
