package com.qf.dao;

import com.qf.entity.Order;
import com.qf.entity.OrderDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IOrderMapper {
    int addOrder(Order order);

    int addOrderDetail(@Param("orderDetailList") List<OrderDetail> orderDetailList);

    List<Order> queryOrderByUid(Integer uid);

    Order queryOrderById(String orderid);

    int updateStatusByOrderId(@Param("orderid") String orderid, @Param("status") Integer status);
}
