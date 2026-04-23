package com.hrbu.aidemo.tool;

import com.hrbu.aidemo.dto.FlightDTO;
import com.hrbu.aidemo.entity.Flight;
import com.hrbu.aidemo.entity.User;
import com.hrbu.aidemo.service.IFlightService;
import com.hrbu.aidemo.service.IUserService;
import com.hrbu.aidemo.service.OrderService;
import com.hrbu.aidemo.util.GaoDeRouteService;
import com.hrbu.aidemo.util.RouteRequest;
import com.hrbu.aidemo.util.RouteResult;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Say my name
 */
@Component("functionTools")
public class FunctionTools {
    private static final Logger log = LoggerFactory.getLogger(FunctionTools.class);
    @Autowired
    private OrderService orderService;
    @Autowired
    private IFlightService flightService;
    @Autowired
    private IUserService userService;

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

    /*
    * 查询航班信息
    * */
    @Tool(name = "queryFlightInfo",value = "查询航班信息")
    public List<Flight> queryFlightInfo(@P("出发日期") String departureDate,
                                        @P("出发地") String departure,
                                        @P("目的地") String destination){
        System.out.println("======大模型传入数据=======");
        System.out.println("出发日期："+departureDate);
        System.out.println("出发地："+departure);
        System.out.println("目的地："+destination);
        FlightDTO flightDTO = new FlightDTO();

        flightDTO.setDepartureDate(LocalDate.parse(departureDate));
        flightDTO.setDeparture(departure);
        flightDTO.setDestination(destination);

        return flightService.queryFlight(flightDTO);
    }

    /*
    * 预定航班
    * */
    @Tool(name = "",value = "使用身份证购买机票,需要用户提供除身份证外的姓名、手机号，邮箱是可选的")
    public boolean bookFlightWithIdCard(
            @P(value = "身份证") String idCard,
            @P("姓名") String name,
            @P(value = "手机号") String phone,
            @P(value = "邮箱",required = false) String email){

        //用户购票信息校验
        boolean b = checkParameter(idCard, name, phone,null);
        User user = new User();
        user.setIdCard(idCard);
        user.setName(name);
        user.setPhoneNum(phone);

        boolean userInfo = userService.createUserInfo(user);
        if (!userInfo){
            return false;
        }

        return true;
    }

    @Tool(name = "bookFlightWithPassport",value = "使用护照号购买机票，需要用户提供除护照号外的姓名，手机号，邮箱是可选的")
    public boolean bookFlightWithPassword(
            @P("姓名") String name,
            @P(value = "手机号") String phone,
            @P(value = "护照号",required = false) String passportNum,
            @P(value = "邮箱",required = false) String email){

        //用户购票信息校验
        boolean b = checkParameter(null, name, phone, passportNum);
        User user = new User();
        user.setPassportNum(passportNum);
        user.setName(name);
        user.setPhoneNum(phone);

        boolean userInfo = userService.createUserInfo(user);
        if (!userInfo){
            return false;
        }

        return true;
    }

    private boolean checkParameter(String idCard,String name,String phone,String passport){
        if ((idCard == null || idCard.isBlank()) && (passport == null || passport.isBlank())){
            return false;
        }

        if (name == null || name.isBlank()){
            return false;
        }

        return phone != null && !phone.isBlank();
    }
}
