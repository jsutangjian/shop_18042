package com.qf.service.shop_serviceimpl.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.qf.dao.IAddressMapper;
import com.qf.entity.Address;
import com.qf.service.IAddressService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service//dubbo的Service
public class AddressServiceImpl implements IAddressService {

    @Autowired
    private IAddressMapper addressMapper;

    @Override
    public List<Address> queryAddressByUid(Integer uid) {
        return addressMapper.queryAddressByUid(uid);
    }

    @Override
    public Address addAddress(Address address) {
        addressMapper.addAddress(address);
        return address;
    }
}
