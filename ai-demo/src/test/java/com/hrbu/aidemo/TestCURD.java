package com.hrbu.aidemo;


import com.hrbu.aidemo.assistant.QwenAssistant;
import com.hrbu.aidemo.dto.FlightDTO;
import com.hrbu.aidemo.entity.ChatMessages;
import com.hrbu.aidemo.entity.Flight;
import com.hrbu.aidemo.entity.User;
import com.hrbu.aidemo.exception.UnsafeSqlException;
import com.hrbu.aidemo.mapper.CommonQueryMapper;
import com.hrbu.aidemo.mapper.UserMapper;
import com.hrbu.aidemo.service.IFlightService;
import com.hrbu.aidemo.util.AmapUtil;
import com.hrbu.aidemo.util.GaoDeRouteService;
import com.hrbu.aidemo.util.RouteRequest;
import com.hrbu.aidemo.util.RouteResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class TestCURD {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private IFlightService flightService;
    @Autowired
    private QwenAssistant qwenAssistant;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CommonQueryMapper commonQueryMapper;
    @Test
    public void testQuery(){
        Criteria criteria = new Criteria("memoryId").is(18);
        Query query = new Query(criteria);
        List<ChatMessages> chatMessages = mongoTemplate.find(query, ChatMessages.class);
        for (ChatMessages chatMessages1 : chatMessages){
            System.out.println(chatMessages1.getContent());
        }
    }

    @Test
    public void queryFlight(){
        FlightDTO flightDTO = new FlightDTO();
        flightDTO.setDestination("上海浦东");
        flightDTO.setDepartureDate(LocalDate.of(2025,4,17));
        flightDTO.setDeparture("北京首都");
        List<Flight> flights = flightService.queryFlight(flightDTO);
        System.out.println(flights);
    }

    @Test
    public void test02(){
        String chat = qwenAssistant.chat("这句话是谁说的，从我给你的知识库里面找："+"Innovation is the intersection of imagination and reality");
        System.out.println(chat);
    }
    @Test
    public void test03(){
        String chat = qwenAssistant.chat("重庆到成都市，帮我规划一条驾车路线，你需要用我给的工具进行调用，如果没有数据就告诉没有这条数据，直接返回我路线即可，比如你没有获取到数据");
        System.out.println(chat);
    }

    @Autowired
    private AmapUtil amapUtil;
    @Test
    public void test04(){
        String amapApiKey = System.getenv("AMAP_API_KEY");
        String airport = amapUtil.geocode("海口机场");
        System.out.println(airport);
    }

    @Autowired GaoDeRouteService gaoDeRouteService;
    @Test
    public void test05(){
        RouteResult result = new RouteResult();
        RouteRequest request = new RouteRequest();
        request.setOrigin(gaoDeRouteService.getGeo("哈尔滨"));
        request.setDestination(gaoDeRouteService.getGeo("北京"));

        RouteResult drivingRoute = gaoDeRouteService.getDrivingRoute(request);
        System.out.println("距离："+(Double.parseDouble(drivingRoute.getDistance())/1000)+"km");
        double hour = Double.parseDouble(drivingRoute.getDuration())%3600;
        double min = (Double.parseDouble(drivingRoute.getDuration()) - hour*3600)%60;

        System.out.println("时间："+hour+":小时"+min+" ：分钟");
        System.out.println("策略："+drivingRoute.getStrategy());
    }

    @Test
    public void test06(){
        String chat = qwenAssistant.chat("加法运算1+1");
        System.out.println(chat);
    }

    /*
    * 测试配置修改数据库配置文件位可读
    * */
    @Test
    public void insertTest(){
        User user = new User();
        user.setId(12L);
        user.setName("test");
        int insert = 0;
        try {
           insert = userMapper.insert(user);

        }catch (Exception e){
            throw new UnsafeSqlException("当前只有可读数据库权限");
        }
        System.out.println(insert>0?"配置只读失败":"配置只读成功");
    }

    @Test
    public void QueryTest(){
        List<Map<String, Object>> maps = commonQueryMapper.executeSelect("select * from user");
        System.out.println(maps);
    }

}
