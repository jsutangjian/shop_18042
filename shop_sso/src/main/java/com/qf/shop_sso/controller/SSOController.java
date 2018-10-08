package com.qf.shop_sso.controller;
import com.alibaba.dubbo.config.annotation.Reference;
import com.google.gson.Gson;
import com.qf.entity.Results;
import com.qf.entity.User;
import com.qf.service.IUserService;

import com.qf.util.ClientUtil;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.apache.http.client.utils.HttpClientUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/sso")
public class SSOController {

    @Reference
    private IUserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping("/tologin")
    public String toLogin(String returnUrl, Model model){//去登录页面首先要借助controller中转，不能直接到登录页
        //tologin这里要做一件事，要携带之前页面的url到登录页面
        model.addAttribute("returnUrl",returnUrl);
        return "login";
    }

    @RequestMapping("/login")
    public String Login(String username, String password,
                        HttpServletResponse response,String returnUrl,
                        @CookieValue(value = "car_token",required = false)String cars){
        //登录的时候合并购物车
        //在登录的时候，从哪个页面，登陆成功就回到哪里去,需要从前端传送过来

        Results<User> results = userService.login(username, password);
        Integer code = results.getCode();
        if(code == 1){//登录成功
            //把用户信息放到redis里
            //生成uuid
            //把uuid作为key，user作为值放到cookie里面
            //跳回首页
            if(returnUrl == null || "".equals(returnUrl)){
                //跳到网站前端页面
                return "redirect:http://localhost:8083";
            }
            String token = UUID.randomUUID().toString();
            redisTemplate.opsForValue().set(token,results.getData());
            redisTemplate.expire(token,7, TimeUnit.DAYS);
            returnUrl.replace("*","&");
            //将token放入cookie，把cookie写入浏览器
            Cookie cookie = new Cookie("login_token",token);
            cookie.setPath("/");//设置从根路径出发，一保存所有项目都能访问
            cookie.setMaxAge(7 * 24 * 60 * 60);//cookie7天过期
            response.addCookie(cookie);
            if(cars != null){
                //有临时购物车
                String url = "http://localhost:8086/car/carhebing";//购物车合并的请求
                Map<String,String> params = new HashMap<String,String>();
                params.put("uid",results.getData().getId()+"");//登录成功用户的为id
                Map<String,String> header = new HashMap<String,String>();
                try {
                    header.put("Cookie","car_token="+ URLEncoder.encode(cars,"utf-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                String responseEntity = ClientUtil.sendPostAndHeader(url, params, header);
                if("success".equals(responseEntity)){
                    //代表购物车合并成功，就要清空cookie里面的信息
                    Cookie cookie1 =  new Cookie("car_token",null);
                    cookie1.setMaxAge(0);
                    cookie1.setPath("/");
                    response.addCookie(cookie1);
                }
            }

           // Cookie cookie = new Cookie();
            //登录成功，返回到商城前台首页
            //"redirect:http://localhost:8083"
            //返回到过来的这个页面
            return "redirect:"+returnUrl;
        }else{
            //登录失败,返回首页
            //return "redirect:http://localhost:8085/sso/tologin";
            return "login";
        }
    }

    @RequestMapping("/islogin")
    @ResponseBody
   /* @CrossOrigin*/
    //是前台ajax发送过来的请求,因为是跨域请求,而且数据传输过去要是json格式的，要导入gson包
    public String isLogin(@CookieValue(value = "login_token", required = false) String login_token,HttpServletRequest request){
       /* Cookie[] cookies = request.getCookies();
        if(cookies!=null){
            for (Cookie cookie : cookies) {
                if("login_token".equals(cookie.getName())){
                    String login_token = cookie.getValue();
                    User user = (User)redisTemplate.opsForValue().get(login_token);
                    return "callback("+new Gson().toJson(user)+")";
                }
            }
        }
        return null;*/
        User user = null;
        if(login_token != null){
            user = (User) redisTemplate.opsForValue().get(login_token);
        }
        return "callback(" + new Gson().toJson(user) + ")";
    }

    @RequestMapping("/logout")
    public String logout(@CookieValue(value = "login_token",required = false)String login_token,
                         HttpServletResponse response){
       //@ CookieValue注解相当于从Cookie去找login_token，设置了为false之后，没找到不会报错，会是null
        //注销，首先需要从redis中删除这个login_token,需要将cookie设置为过期
        redisTemplate.delete(login_token);
        //设置Cookie过期
        Cookie cookie = new Cookie(login_token,null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        //设置过期之后，返回到前端首页
        return "redirect:http://localhost:8083";
    }
}
