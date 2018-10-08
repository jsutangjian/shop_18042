package com.qf.shop.shop_back.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.service.IShopService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

    /**
     * 导入dubbo的自动注入refernce
     */
    @Reference
    private IShopService shopService;

    @RequestMapping("/")
    public String toIndex(){
        return "index";
    }

    @RequestMapping("/topage/{addgoods}")
    public String toPage(@PathVariable("addgoods") String pageName){
        return pageName;
    }
}
