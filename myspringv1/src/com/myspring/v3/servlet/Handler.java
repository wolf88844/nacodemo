package com.myspring.v3.servlet;

import com.myspring.annotation.MyRequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @ClassName Handler
 * @Author LIUHANPENG
 * @Date 2020/1/16 0016 9:20
 **/
public class Handler {
    protected Object controller;
    protected Method method;
    protected Pattern pattern;
    protected Map<String,Integer> paramIndexMapping;

    public Handler(Pattern pattern,Object controller,Method method){
        this.controller = controller;
        this.method = method;
        this.pattern = pattern;
        paramIndexMapping = new HashMap<>();
        putParamIndexMapping(method);
    }

    private void putParamIndexMapping(Method method) {
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        for (int i = 0; i < parameterAnnotations.length; i++) {
            for (Annotation a:parameterAnnotations[i]) {
                if(a instanceof MyRequestParam){
                    String paramName = ((MyRequestParam) a).value();
                    if(!"".equals(paramName)){
                        paramIndexMapping.put(paramName,i);
                    }
                }
            }
        }

        Class<?>[] parameterTypes = method.getParameterTypes();
        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> type = parameterTypes[i];
            if(type== HttpServletResponse.class || type == HttpServletRequest.class){
                paramIndexMapping.put(type.getName(),i);
            }
        }
    }
}
