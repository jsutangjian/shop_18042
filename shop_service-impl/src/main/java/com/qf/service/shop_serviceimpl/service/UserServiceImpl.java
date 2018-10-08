package com.qf.service.shop_serviceimpl.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.qf.dao.IUserMapper;
import com.qf.entity.Results;
import com.qf.entity.User;
import com.qf.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUserMapper userDao;

    @Override
    public Results<User> login(String username, String password) {
        Integer code = null;
        String msg = null;
        User user = userDao.queryByUserName(username);
        if(user != null){
            if(password.equals(user.getPassword())){
                //用户名密码正确
                code = 1;
                msg = "登录成功!";
            }else{
                code = 2;
                msg ="密码错误";
            }
        }else{
            code = 3;
            msg = "用户名不存在";
        }
        Results<User> results = new Results<User>(code,msg,user);
        return results;
    }
}
