package com.panch.service;

import com.panch.domain.Role;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author xsj007
* @description 针对表【sys_role(角色信息表)】的数据库操作Service
* @createDate 2023-04-23 21:37:25
*/
public interface RoleService extends IService<Role> {

    List<String> selectRoleKeyByUserId(Long id);
}
