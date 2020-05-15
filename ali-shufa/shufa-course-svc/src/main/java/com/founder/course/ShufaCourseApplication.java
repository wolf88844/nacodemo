package com.founder.course;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @ClassName ShufaCourseApplication
 * @Author LIUHANPENG
 * @Date 2020/5/15 0015 12:34
 **/
@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
@RefreshScope
public class ShufaCourseApplication {
    public static void main(String[] args){
        SpringApplication.run(ShufaCourseApplication.class,args);
    }
}
