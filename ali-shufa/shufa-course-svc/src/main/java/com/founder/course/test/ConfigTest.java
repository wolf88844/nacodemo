package com.founder.course.test;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName ConfigTest
 * @Author LIUHANPENG
 * @Date 2020/5/15 0015 13:02
 **/
@RestController
@RequestMapping("config")
public class ConfigTest {
    @Value("${useCache:false}")
    public boolean useCache;

    public void setUseCache(boolean useCache){
        this.useCache  = useCache;
    }

    @GetMapping("/get")
    public boolean get(){
        return useCache;
    }
}
