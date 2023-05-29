package com.panch.mapper;

import com.panch.domain.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author xsj007
* @description 针对表【sys_role(角色信息表)】的数据库操作Mapper
* @createDate 2023-04-23 21:37:25
* @Entity com.panch.domain.Role
*/
public interface RoleMapper extends BaseMapper<Role> {

    List<String> selectRoleKeyByUserId(Long id);
}




