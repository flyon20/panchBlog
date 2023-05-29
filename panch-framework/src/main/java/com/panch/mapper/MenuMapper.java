package com.panch.mapper;

import com.panch.domain.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author xsj007
* @description 针对表【sys_menu(菜单权限表)】的数据库操作Mapper
* @createDate 2023-04-23 20:17:57
* @Entity com.panch.domain.Menu
*/
public interface MenuMapper extends BaseMapper<Menu> {

    List<String> selectPermsByUserId(Long id);

    List<Menu> selectAllRouterMenu();

    List<Menu> selectRouterMenuTreeByUserId(Long userId);
}




