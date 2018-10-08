package com.qf.service.shop_serviceimpl.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.qf.dao.IAddressMapper;
import com.qf.dao.ICarMapper;
import com.qf.dao.IOrderMapper;
import com.qf.entity.Address;
import com.qf.entity.Car;
import com.qf.entity.Order;
import com.qf.entity.OrderDetail;
import com.qf.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private ICarMapper carMapper;

    @Autowired
    private IAddressMapper addressMapper;

    @Autowired
    private IOrderMapper orderMapper;

    @Override
    @Transactional
    public String submitOrderAndOrderDetail(Integer[] cids, Integer aid, Integer uid) {
        //首先根据购物车id查询购物车列表
        //select * from car where cid in (,,,,)
        List<Car> carList = carMapper.queryByCids(cids);

        //根据地址id查询收货地址的详情
        Address address = addressMapper.queryAddressById(aid);

        //计算订单总价
        Double totalPrice = 0.0;
        for (Car car : carList) {
            totalPrice += car.getGoods().getPrice()*car.getGnumber();
        }

        //生成订单
        Order order = new Order();
        order.setAddress(address.getAddress());
        order.setCode(address.getCode());
        order.setOrderid(UUID.randomUUID().toString());
        order.setOprice(totalPrice);
        order.setOrdertime(new Date());
        order.setPerson(address.getPerson());
        order.setStatus(0);//0代表未支付
        order.setUid(uid);

        orderMapper.addOrder(order);//添加订单

        //添加订单详情,一条详情对应一个订单
        List<OrderDetail> orderDetailList = new ArrayList<OrderDetail>();
        for (Car car : carList) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setGcount(car.getGnumber());
            orderDetail.setGid(car.getGid());
            orderDetail.setGimage(car.getGoods().getGimage());
            orderDetail.setGinfo(car.getGoods().getGinfo());
            orderDetail.setGname(car.getGoods().getTitle());
            orderDetail.setOid(order.getId());
            orderDetail.setPrice(car.getGoods().getPrice());

            orderDetailList.add(orderDetail);
        }
        //添加商品详情
        orderMapper.addOrderDetail(orderDetailList);

        //生成订单之后要删除购物车
        for (Car car : carList) {
            carMapper.deleteById(car.getId());
        }
        return order.getOrderid();//返回主键回填的id
    }

    @Override
    public List<Order> queryOrderByUid(Integer uid) {
        return orderMapper.queryOrderByUid(uid);
    }

    @Override
    public Order queryOrderById(String orderid) {
        return orderMapper.queryOrderById(orderid);
    }

    @Override
    public int updateStatusByOrderId(String orderid, Integer status) {
        return orderMapper.updateStatusByOrderId(orderid,status);
    }
}
