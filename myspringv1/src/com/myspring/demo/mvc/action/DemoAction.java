package com.myspring.demo.mvc.action;

import com.myspring.annotation.MyAutowired;
import com.myspring.annotation.MyController;
import com.myspring.annotation.MyRequestMapping;
import com.myspring.annotation.MyRequestParam;
import com.myspring.demo.service.IDemoService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName DemoAction
 * @Author LIUHANPENG
 * @Date 2020/1/15 0015 14:37
 **/
@MyController
@MyRequestMapping("/demo")
public class DemoAction {
    @MyAutowired
    private IDemoService demoService;

    @MyRequestMapping("/query")
    public void query(HttpServletRequest req, HttpServletResponse resp, @MyRequestParam("name") String name){
        String result = demoService.get(name);
        try {
            resp.getWriter().write(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @MyRequestMapping("/add")
    public void add(HttpServletRequest request,HttpServletResponse response,
                    @MyRequestParam("a") int a,@MyRequestParam("b") int b){
        try {
            response.getWriter().write(a+"+"+b+"="+(a+b));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
