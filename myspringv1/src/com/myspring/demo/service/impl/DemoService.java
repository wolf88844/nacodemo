package com.myspring.demo.service.impl;

import com.myspring.annotation.MyService;
import com.myspring.demo.service.IDemoService;

/**
 * @ClassName DemoService
 * @Author LIUHANPENG
 * @Date 2020/1/15 0015 14:36
 **/
@MyService
public class DemoService implements IDemoService {
    @Override
    public String get(String name) {
        return "My name is "+name;
    }
}
