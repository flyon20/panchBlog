package com.panch.service;

import com.panch.domain.ResponseResult;
import com.panch.domain.User;

public interface BlogLoginService {
    ResponseResult login(User user);

    ResponseResult logout();
}
