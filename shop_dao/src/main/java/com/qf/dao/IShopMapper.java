package com.qf.dao;

import com.qf.entity.Goods;

import java.util.List;

public interface IShopMapper {
    public List<Goods> queryAll();

    int addgoods(Goods good);

    List<Goods> queryNewGoods();

    Goods queryById(Integer id);
}
