package com.qf.dao;

import com.qf.entity.Car;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ICarMapper {
    int addToCar(Car car);
    List<Car> queryAllByUid(Integer uid);
    int deleteById(Integer id);
    int deleteAllByUid(Integer uid);

    List<Car> queryByGidsAndUid(@Param("gids") Integer[] gids, @Param("uid") Integer uid);

    List<Car> queryByCids(@Param("cids") Integer[] cids);
}
