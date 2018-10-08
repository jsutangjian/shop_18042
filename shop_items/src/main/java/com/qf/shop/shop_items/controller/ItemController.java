package com.qf.shop.shop_items.controller;

import com.qf.entity.Goods;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/item")
public class ItemController {

    @Autowired
    private Configuration configuration;

    @RequestMapping("/newhtml")
    public String createHtml(@RequestBody Goods good, HttpServletRequest request){
        //前端那边拿不了直接的路径，需要传过去
        Writer writer = null;
        try {
           //指定生成静态页面的位置
           Template template = configuration.getTemplate("newhtml.ftl");
           String classpath = this.getClass().getResource("/").getPath();
           //classpath:/D:/Myidea/1/shop_1804/shop_items/target/classes/
           Map<String ,Object> map = new HashMap<String,Object>();
           //把传过来的商品goods传过去
           map.put("good",good);
           map.put("contexPath",request.getContextPath());
           //生成静态页面的名字以商品的id命名
           //都放在classpath下的static/spage（静态页面文件下）
           writer = new FileWriter(classpath +"static/spage/"+good.getId()+".html");
           template.process(map,writer);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        } finally {
            if(writer != null){
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
       }
        return null;
    }
}
