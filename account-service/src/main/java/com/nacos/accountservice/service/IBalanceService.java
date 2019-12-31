package com.nacos.accountservice.service;

import com.nacos.accountservice.pojo.Balance;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @ClassName BalanceService
 * @Author LIUHANPENG
 * @Date 2019/12/31 0031 17:18
 **/
@FeignClient(name = "payment-service",fallback = IBalanceService.BalanceServiceFallback.class)
public interface IBalanceService {
    @RequestMapping(value = "/pay/balance",method = RequestMethod.GET)
    Balance getBalance(@RequestParam("id") Integer id);

    @Component
    class BalanceServiceFallback implements IBalanceService{
        @Override
        public Balance getBalance(Integer id) {
            return new Balance(0,0,0,"降级");
        }
    }
}


