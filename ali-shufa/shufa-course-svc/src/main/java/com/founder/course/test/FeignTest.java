package com.founder.course.test;

import com.founder.course.api.client.ShufaCourseFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName FeignTest
 * @Author LIUHANPENG
 * @Date 2020/5/15 0015 12:04
 **/
@RestController
public class FeignTest {
    @Autowired
    private ShufaCourseFeignClient shufaCourseFeignClient;

    @GetMapping("/feign")
    public String test(){
        return "feign获取："+shufaCourseFeignClient.configGet();
    }
}
