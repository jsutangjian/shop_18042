package com.qf.shop_order.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.entity.Address;
import com.qf.entity.Car;
import com.qf.entity.Order;
import com.qf.entity.User;
import com.qf.service.IAddressService;
import com.qf.service.ICarService;
import com.qf.service.IOrderService;
import com.qf.util.IsLogin;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Reference
    private ICarService cartService;

    @Reference
    private IAddressService addressService;

    @Reference
    private IOrderService orderService;

    @IsLogin(tologin = true)//必须要登录才能到订单页面，不然不知道订单属于谁的
    @RequestMapping("/orderedit")
    public String orderEdit(Integer[] gid, User user, Model model){
        System.out.println("商品id为:"+ Arrays.toString(gid));
        System.out.println("登录的用户为:"+user);
        //根据购物车商品的id和用户的id来查询需要购买哪些商品
        List<Car> carList = cartService.queryByGidsAndUid(gid, user.getId());
        //根据用户id查询收货地址,用户和地址是一对多的关系

        List<Address> addressList = addressService.queryAddressByUid(user.getId());
        model.addAttribute("carList",carList);
        model.addAttribute("addressList",addressList);
        return "orderEdit";
    }

    @IsLogin//因为加入订单的时候必须登录，这里就没有设置必须登录了
    @RequestMapping("/addAddress")
    @ResponseBody
    public Address addAddress(Address address ,User user){
        System.out.println("登录的用户名为:"+user);
        address.setUid(user.getId());
        addressService.addAddress(address);
        System.out.println("新增收获地址为:"+address);
        return address;
    }
    @IsLogin
    @RequestMapping("/ordersubmit")
    @ResponseBody
    public String sumbitOrder(Integer[] cid,Integer aid,User user){
        System.out.println("地址id为"+aid);
        System.out.println("购物车id为"+Arrays.toString(cid));
        //提交订单,这里要用到订单和订单详情
        String orderid = null;
        try{
            orderid = orderService.submitOrderAndOrderDetail(cid,aid,user.getId());
        }catch (Exception e){
            e.printStackTrace();
        }
        return orderid;
    }

    //从首页进来，点击我的订单查看订单
    @IsLogin
    @RequestMapping("/myorder")
    public String myOrder(User user,Model model){
        //登录的用户，通过用户查看我的订单
        System.out.println("登录的用户为:"+user);
        List<Order> orderList = orderService.queryOrderByUid(user.getId());
        System.out.println("订单列表:"+orderList);
        model.addAttribute("orderList",orderList);
        return "myorder";
    }
}
