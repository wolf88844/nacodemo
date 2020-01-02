package com.example.auth2.controller;

import com.example.auth2.entity.Result;
import com.example.auth2.enumeration.ResultCode;
import com.example.auth2.service.MyUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * @ClassName MemberController
 * @Author LIUHANPENG
 * @Date 2020/1/2 0002 17:30
 **/
@RestController
@RequestMapping("/api")
public class MemberController {

    @Autowired
    private MyUserDetailService userDetailService;
    @Autowired
    private ConsumerTokenServices consumerTokenServices;

    @GetMapping("/member")
    public Principal user(Principal member){
        return member;
    }

    @DeleteMapping(value = "/exit")
    public Result revokeToken(String access_token){
        Result result = new Result();
        if(consumerTokenServices.revokeToken(access_token)){
            result.setCode(ResultCode.SUCCESS.getCode());
            result.setMessage("注销成功");
        }else{
            result.setCode(ResultCode.FAILED.getCode());
            result.setMessage("注销失败");
        }
        return result;
    }
}
