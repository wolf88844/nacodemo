package com.nacos.accountservice.controller;

import com.nacos.accountservice.pojo.Balance;
import com.nacos.accountservice.pojo.User;
import com.nacos.accountservice.service.BalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName AccountController
 * @Author LIUHANPENG
 * @Date 2019/12/31 0031 17:03
 **/
@RestController
public class AccountController {

    @Value("${sleep:0}")
    private String sleep;

    final static Map<Integer, User> userMap = new HashMap(){{
        put(1,new User(1,"张三"));
        put(2,new User(2,"李四"));
        put(3,new User(3,"王五"));
    }};

    @RequestMapping("/acc/user")
    public User getUser(@RequestParam Integer id){
        System.out.println("request:/acc/user?id="+id+",sleep:"+sleep);
        int sleepInt = Integer.valueOf(sleep);
        if (sleepInt>0){
            try{
                Thread.sleep(sleepInt);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        if(id != null && userMap.containsKey(id)){
            User user = userMap.get(id);
            return user;
        }
        return new User(0,"");
    }
}
