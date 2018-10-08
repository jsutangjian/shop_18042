package com.qf.service.shop_serviceimpl.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.qf.dao.ICarMapper;
import com.qf.entity.Car;
import com.qf.entity.Goods;
import com.qf.entity.User;
import com.qf.service.ICarService;
import com.qf.service.IShopService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class CarServiceImpl implements ICarService {

    @Autowired
    private ICarMapper carMapper;

    @Autowired
    private IShopService shopService;

    @Override
    public int addToCar(Car car) {
        return carMapper.addToCar(car);
    }

    @Override
    public List<Car> queryAllByUid(Integer uid) {
        return carMapper.queryAllByUid(uid);
    }

    @Override
    public int deleteById(Integer id) {
        return carMapper.deleteById(id);
    }

    @Override
    public int deleteAllByUid(Integer uid) {
        return carMapper.deleteAllByUid(uid);
    }

    @Override
    public List<Car> getCarList(String cars, User user) {
        List<Car> carList = null;
        if(user != null){
            //如果已经登录，从数据库中查询列表
            carList = carMapper.queryAllByUid(user.getId());
        }else{
            //未登录，先把cars转成购物车列表
            TypeToken<List<Car>> typeToken = new TypeToken<List<Car>>(){};
            carList = new Gson().fromJson(cars,typeToken.getType());
        }
        if(carList != null){
            for(int i=0;i<carList.size();i++){
                Goods goods = shopService.queryById(carList.get(i).getGid());
                carList.get(i).setGoods(goods);
            }
        }
        return carList;
    }

    @Override
    public List<Car> queryByGidsAndUid(Integer[] gids, Integer uid) {
        //根据用户id和商品的id查询订单有哪些商品
        List<Car> carList = carMapper.queryByGidsAndUid(gids, uid);
        //查询购物车的记录，并且查询购物车商品的记录
       for (int i = 0; i < carList.size(); i++) {
           Goods goods = shopService.queryById(carList.get(i).getGid());
           carList.get(i).setGoods(goods);
       }
        return carList;
    }
}
