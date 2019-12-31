package com.nacos.paymentservice.controller;

import com.nacos.paymentservice.pojo.Balance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName PaymentController
 * @Author LIUHANPENG
 * @Date 2019/12/31 0031 14:46
 **/
@RestController
@RefreshScope
public class PaymentController {

    @Value(value = "${sleep:0}")
    private String sleep;

    final static Map<Integer, Balance> balanceMap = new HashMap(){{
        put(1,new Balance(1,10,1000));
        put(2,new Balance(2,0,10000));
        put(3,new Balance(3,100,0));
    }};

    @RequestMapping("/pay/balance")
    public Balance getBalance(Integer id){
        System.out.println("request:/pay/balance?id="+id+",sleep:"+sleep);
        int sleepInt = Integer.valueOf(sleep);
        if (sleepInt>0){
            try{
                Thread.sleep(sleepInt);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        if(id != null && balanceMap.containsKey(id)){
            return balanceMap.get(id);
        }
        return new Balance(0,0,0);
    }
}
