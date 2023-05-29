package com.panch.service;

import com.panch.domain.ResponseResult;
import com.panch.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author xsj007
* @description 针对表【sys_user(用户表)】的数据库操作Service
* @createDate 2023-04-17 12:08:29
*/
public interface UserService extends IService<User> {

    ResponseResult userInfo();

    ResponseResult updateUserInfo(User user);

    ResponseResult register(User user);
}
