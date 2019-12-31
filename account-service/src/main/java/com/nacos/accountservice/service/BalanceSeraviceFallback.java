package com.nacos.accountservice.service;

import com.nacos.accountservice.pojo.Balance;
import org.springframework.stereotype.Component;

/**
 * @ClassName BalanceSeraviceFallback
 * @Author LIUHANPENG
 * @Date 2019/12/31 0031 17:20
 **/
@Component
public class BalanceSeraviceFallback implements BalanceService{
    @Override
    public Balance getBalance(Integer id) {
        return new Balance(0,0,0,"降级");
    }
}
