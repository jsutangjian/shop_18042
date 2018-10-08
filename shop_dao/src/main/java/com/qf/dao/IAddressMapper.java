package com.qf.dao;

import com.qf.entity.Address;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IAddressMapper {
    List<Address> queryAddressByUid(Integer uid);

    int addAddress(Address address);

    Address queryAddressById(@Param("id") Integer aid);
}
