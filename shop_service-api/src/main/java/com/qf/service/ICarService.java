package com.qf.service;

import com.qf.entity.Car;
import com.qf.entity.User;

import java.util.List;

public interface ICarService {
    int addToCar(Car car);
    List<Car> queryAllByUid(Integer uid);
    int deleteById(Integer id);
    int deleteAllByUid(Integer uid);
    List<Car> getCarList(String cars,User user);
    List<Car> queryByGidsAndUid(Integer[] gids,Integer uid);
}
