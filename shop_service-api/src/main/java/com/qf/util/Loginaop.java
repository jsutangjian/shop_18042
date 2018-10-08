package com.qf.util;

import com.qf.entity.User;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Aspect
public class Loginaop {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private RedisTemplate redisTemplate;

    @Around("execution(* *..*Controller.*(..))&& @annotation(com.qf.util.IsLogin)")
    //任意返回值，任意包下以Controller结尾的方法的任意参数，只要加了IsLogin这个注解就会增强
    public Object isLogin(ProceedingJoinPoint proceedingJoinPoint){

        //获取注解


        MethodSignature signature = (MethodSignature)proceedingJoinPoint.getSignature();
        Method method = signature.getMethod();
        IsLogin isLogin = method.getAnnotation(IsLogin.class);//得到注解的实体类
        System.out.println("是否跳到登录页"+isLogin.tologin());


        String token = null;

        //判断是否登录
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for(Cookie cookie : cookies){
                if(cookie.getName().equals("login_token")){
                    token = cookie.getValue();
                    break;
                }
            }
        }

        User user = null;

        if(token != null){
            //查询redis
            user = (User) redisTemplate.opsForValue().get(token);
        }


        //判断是否需要跳转到登录页页面 - 有些方法可能需要强制登录
        if(user == null && isLogin.tologin()){
            String returnUrl = request.getRequestURL() + "?" + request.getQueryString();
            returnUrl = returnUrl.replace("&", "*");
            return "redirect:http://localhost:8085/sso/tologin?returnUrl=" + returnUrl;
        }


        Object[] args = proceedingJoinPoint.getArgs();
        //已经登录
        //获得目标方法的实参列表
        for(int i = 0; i < args.length; i++){
            if(args[i] != null && args[i].getClass()==User.class){
                args[i] = user;
            }
        }


        Object obj = null;
        try {
            obj = proceedingJoinPoint.proceed(args);//放行目标方法
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return obj;

        /*Cookie[] cookies = request.getCookies();
        String token = null;
        if(cookies != null){//首先要判断cookie不为null
            for (Cookie cookie : cookies) {
                if("login_token".equals(cookie.getName())){
                    token = cookie.getValue();
                    break;
                }
            }
        }
        User user = null;
        if(token != null){
            //如果token不为空，还要判断redis里有没有
            user = (User)redisTemplate.opsForValue().get(token);
            //查出来的一定是一个user

        }
        System.out.println("user为:"+user);
        Object obj = null;
        Object[] args = proceedingJoinPoint.getArgs();
        if(user != null && args != null){//已经登录
            for(int i=0;i<args.length;i++){
                if(args[i] != null && args[i].getClass() == User.class){
                    args[i]=user;//把user放到参数里面去
                    System.out.println(user);
                }
            }
        }

        if(user == null && isLogin.tologin()){
            //如果没有登录的，并且请求是要到登录页面的，就要跳转到登录页面
            String returnUrl = request.getRequestURI()+"?"+request.getQueryString();

           // returnUrl = returnUrl.replace("&", "*");
            System.out.println("returnUrl:"+returnUrl);
            System.out.println("user的值是:"+user);
            return "redirect:http://localhost:8085/sso/tologin?returnUrl="+returnUrl;
        }
        try {
            System.out.println("目标方法之前执行");
            //放行的时候用这种带参数的方式放行
            obj = proceedingJoinPoint.proceed(args);
            System.out.println("目标方法之后执行");
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        System.out.println("user等于"+user);
        return obj;*/
    }
}
