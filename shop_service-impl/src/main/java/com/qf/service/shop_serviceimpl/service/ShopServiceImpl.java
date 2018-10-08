package com.qf.service.shop_serviceimpl.service;


import com.alibaba.dubbo.config.annotation.Service;
import com.qf.dao.IShopMapper;
import com.qf.entity.Goods;
import com.qf.service.IShopService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 通过dubbo的Service注解去查看dao层是否有这个应用，有就返回ip地址和端口，让它访问
 */
@Service
public class ShopServiceImpl implements IShopService {

    //这里main采用自动注入
    @Autowired
    private IShopMapper shopMapper;
    @Override
    public List<Goods> queryAll() {
        return shopMapper.queryAll();
    }

    @Override
    public Goods addgoods(Goods good) {
        int gid = shopMapper.addgoods(good);
        //System.out.println("主键回填为:"+good.getId());
        return good;
    }

    @Override
    public List<Goods> queryNewGoods() {
        return shopMapper.queryNewGoods();
    }

    @Override
    public Goods queryById(Integer id) {
        return shopMapper.queryById(id);
    }
}
