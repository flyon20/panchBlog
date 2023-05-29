package com.panch.service;

import com.panch.domain.Menu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author xsj007
* @description 针对表【sys_menu(菜单权限表)】的数据库操作Service
* @createDate 2023-04-23 20:17:57
*/
public interface MenuService extends IService<Menu> {

    List<String> selectPermsByUserId(Long id);

    List<Menu> selectRouterMenuTreeByUserId(Long userId);
}
