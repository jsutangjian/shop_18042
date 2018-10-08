package com.qf.util;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)//在运行时加的一个注解
@Target(ElementType.METHOD)//是加在方法上的
public @interface IsLogin {
    boolean tologin() default  false;//方法默认返回值是false
}
