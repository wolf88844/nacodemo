package com.myspring.annotation;

import java.lang.annotation.*;

/**
 * @ClassName MyService
 * @Author LIUHANPENG
 * @Date 2020/1/15 0015 14:30
 **/
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyService {
    String value() default "";
}
