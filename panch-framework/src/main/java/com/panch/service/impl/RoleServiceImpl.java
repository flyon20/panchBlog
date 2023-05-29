package com.panch.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.panch.domain.Role;
import com.panch.service.RoleService;
import com.panch.mapper.RoleMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
* @author xsj007
* @description 针对表【sys_role(角色信息表)】的数据库操作Service实现
* @createDate 2023-04-23 21:37:25
*/
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role>
    implements RoleService{

    @Override
    public List<String> selectRoleKeyByUserId(Long id) {
        //判断是否是管理员 如果是返回集合中只需要有admin
        if(id == 1L){
            List<String> roleKeys = new ArrayList<>();
            roleKeys.add("admin");
            return roleKeys;
        }
        //否则查询用户所具有的角色信息
        return getBaseMapper().selectRoleKeyByUserId(id);
    }
}




