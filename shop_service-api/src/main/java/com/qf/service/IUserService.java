package com.qf.service;

import com.qf.entity.Results;
import com.qf.entity.User;

public interface IUserService {
    Results<User> login(String username,String password);
}
