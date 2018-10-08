package com.qf.service;

import com.qf.entity.Goods;

import java.util.List;

public interface IShopService {
    public List<Goods> queryAll();

    Goods addgoods(Goods good);

    List<Goods> queryNewGoods();

    Goods queryById(Integer id);
}
