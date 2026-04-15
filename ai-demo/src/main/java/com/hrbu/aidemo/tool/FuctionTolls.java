package com.hrbu.aidemo.tool;

import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Component;

@Component
public class FuctionTolls {
    @Tool(name = "add",value = "计算两数相加")
    public double add(double a,double b){
        System.out.println("加法运算");
        return a+b;
    }
    @Tool(name = "multiply",value = "计算两数相乘")
    public double multi(double a,double b){
        System.out.println("乘法运算");
        return a*b;
    }
}
