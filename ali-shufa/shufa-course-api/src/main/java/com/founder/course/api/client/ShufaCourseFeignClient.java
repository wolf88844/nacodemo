package com.founder.course.api.client;

import com.founder.course.ShufaCourseConstant;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @ClassName FeginClient
 * @Author LIUHANPENG
 * @Date 2020/5/15 0015 11:59
 **/
@FeignClient(name = ShufaCourseConstant.SERVICE_NAME,fallback = ShufaCourseHystrix.class)
public interface ShufaCourseFeignClient {
    @GetMapping("/config/get")
    String configGet();
}
