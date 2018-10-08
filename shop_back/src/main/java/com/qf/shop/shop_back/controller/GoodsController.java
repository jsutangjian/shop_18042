package com.qf.shop.shop_back.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.google.gson.Gson;
import com.qf.entity.Goods;
import com.qf.service.IShopService;
import com.qf.util.ClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * 对后台工程做了修改
 */
@Controller
@RequestMapping("/goods")
public class GoodsController {

    /**
     * 必须要在main类里面带入fdfs的类，图片储存要用到FastFileStorageClient接口
     */
    @Autowired
    private FastFileStorageClient fastFileStorageClient;

    @Value("${image.path}")
    private String path;

    @Reference
    private IShopService shopService;

    @RequestMapping("/queryall")
    public String queryAll(Model model){
        List<Goods> goodsList = shopService.queryAll();
        model.addAttribute("goodsList", goodsList);
        //将图片上传的路径写在配置文件里面，传到前端去
        model.addAttribute("path",path);//每一个都给他拼接一个path
        return "goodslist";
    }

    @RequestMapping("/addgoods")
    public String addGoods(@RequestParam("file") MultipartFile file, Goods good) throws IOException {

        StorePath storePath = fastFileStorageClient.uploadImageAndCrtThumbImage(file.getInputStream(), file.getSize(), "jpg", null);

        String fullPath = storePath.getFullPath();//得到全路径名
        good.setGimage(fullPath);
        good = shopService.addgoods(good);
         //添加完了之后，重定向回首页
        //在添加商品的时候就要把商品添加到索引库
        //搜索这里端口号是8082
        String result = ClientUtil.sendWithJsonPost("http://localhost:8082/solr/add", new Gson().toJson(good));
        ClientUtil.sendWithJsonPost("http://localhost:8084/item/newhtml", new Gson().toJson(good));
        return "redirect:/goods/queryall";
    }

    /**
     * 搜索新品,第一种方式，利用jquery的漏洞实现浏览器跨域
     */
   /* @RequestMapping("/querynew")
    @ResponseBody
    public String queryNewGoods(){
        List<Goods> goods =  shopService.queryNewGoods();
        System.out.println(goods);
        return "show('"+new Gson().toJson(goods)+"')";
    }*/

    /**
     * 利用springmvc的方式实现跨域
     * @return
     */
    @RequestMapping("/querynew")
    @ResponseBody
    @CrossOrigin
    public List<Goods> queryNewGoods(){
        List<Goods> goods =  shopService.queryNewGoods();
        return goods;
    }
}
