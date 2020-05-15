package com.founder.course.api.client;

import org.springframework.stereotype.Component;

/**
 * @ClassName FeginHystrix
 * @Author LIUHANPENG
 * @Date 2020/5/15 0015 12:00
 **/
@Component
public class ShufaCourseHystrix implements ShufaCourseFeignClient{
    @Override
    public String configGet() {
        return "请求超时";
    }
}
