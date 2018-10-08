package com.qf.dao;

import com.qf.entity.Results;
import com.qf.entity.User;

public interface IUserMapper {
    User queryByUserName(String username);
}
