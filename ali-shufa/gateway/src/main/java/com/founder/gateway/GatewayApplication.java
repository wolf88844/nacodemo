package com.founder.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @ClassName GatewayApplication
 * @Author LIUHANPENG
 * @Date 2020/5/15 0015 13:47
 **/
@SpringBootApplication
@EnableDiscoveryClient
public class GatewayApplication {
    public static void main(String[] args){
        SpringApplication.run(GatewayApplication.class,args);
    }
}
