package com.qf.service;

import com.qf.entity.Order;

import java.util.List;

public interface IOrderService {
    String submitOrderAndOrderDetail(Integer[] cids, Integer aid, Integer id);

    List<Order> queryOrderByUid(Integer uid);

    Order queryOrderById(String orderid);

    int updateStatusByOrderId(String orderid, Integer status);
}
