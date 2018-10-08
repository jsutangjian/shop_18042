package com.qf.shop_car.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qf.entity.Car;
import com.qf.entity.User;
import com.qf.service.ICarService;
import com.qf.service.IShopService;
import com.qf.util.IsLogin;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/car")
public class CarController {

    @Reference
    private ICarService carService;

    @Reference
    private IShopService shopService;

    @IsLogin//加此注解，就是为了看用户有没有登录，有登录，直接返回登录的用户信息，没有就为null
    @RequestMapping("/addtocar")
   // @ResponseBody
    public String addToCar(Car car, User user,
                           HttpServletResponse response,
                           @CookieValue(value = "car_token",required = false)String cars){

        //判断当前是否登录
        if(user != null){
            //添加到数据库
            car.setUid(user.getId());
            carService.addToCar(car);
        } else {
            //添加到cookie中

            List<Car> carList = null;
            if(cars != null){
                //cookie中已经有购物车信息
                TypeToken<List<Car>> tt = new TypeToken<List<Car>>(){};
                carList = new Gson().fromJson(cars, tt.getType());
                carList.add(car);
            } else {
                //cookie中没有购物车信息
                //cookie - key/value必须是string  cookie中不能有中文  cookie最多只能放4K的数据
                carList = Collections.singletonList(car);
            }

            String json = new Gson().toJson(carList);
            try {
                json = URLEncoder.encode(json, "utf-8"); // [{:}] %5B%7B%22gid%22%3A74%2C%22gnumber%22%3A3%7D%5D
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Cookie cookie = new Cookie("car_token", json);
            cookie.setMaxAge(30 * 24 * 60 * 60);
            cookie.setPath("/");

            response.addCookie(cookie);
        }

        return "addToCarSucc";

    }

    @RequestMapping("/carhebing")
    @ResponseBody
    public String CarHeBing(Integer uid,@CookieValue(value = "car_token",required = false)String cars){

        //购物车信息合并，登录的时候就进行合并,把cars转化为List<Car> 对象
        //得到购物车集合
        TypeToken<List<Car>> typeToken = new TypeToken<List<Car>>(){};
        List<Car> carList = (List<Car>)new Gson().fromJson(cars, typeToken.getType());
        for (Car car : carList) {
            //给购物车设置用户id，并存放到数据库中
            car.setUid(uid);
            carService.addToCar(car);
        }

        return "success";
    }

    @IsLogin
    @RequestMapping("/getcarlist")
    @ResponseBody
    public String getCarList(@CookieValue(value = "car_token",required = false)String cars,User user){
       /* List<Car> carList = null;
        if(user != null){
            //如果已经登录，从数据库中查询列表
            carList = carService.queryAllByUid(user.getId());
        }else{
            //未登录，先把cars转成购物车列表
            TypeToken<List<Car>> typeToken = new TypeToken<List<Car>>(){};
            carList = new Gson().fromJson(cars,typeToken.getType());
        }
        if(carList != null){
            for(int i=0;i<carList.size();i++){
                Goods goods = shopService.queryById(carList.get(i).getGid());
                carList.get(i).setGoods(goods);
            }
        }*/
       //封装该方法到ServiceImpl里面
        List<Car> carList = carService.getCarList(cars, user);
        System.out.println(carList);
        return "getCarList("+new Gson().toJson(carList)+")";//用jsonp的方式来做

    }

    //去购物车列表
    @IsLogin
    @RequestMapping("/tocarlist")
    public String toCarList(@CookieValue(value = "car_token",required = false)String cars,
                            User user,Model model){
       List<Car> carList = carService.getCarList(cars, user);
        System.out.println("carList:"+carList);
        model.addAttribute("cars",carList);
        return "carList";
    }
}
