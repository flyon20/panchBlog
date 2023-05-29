package com.panch.controller;

import com.panch.domain.LoginUser;
import com.panch.domain.Menu;
import com.panch.domain.ResponseResult;
import com.panch.domain.User;
import com.panch.domain.vo.AdminUserInfoVo;
import com.panch.domain.vo.MenuVo;
import com.panch.domain.vo.RoutersVo;
import com.panch.domain.vo.UserInfo;
import com.panch.enums.AppHttpCodeEnum;
import com.panch.exception.SystemException;
import com.panch.service.LoginService;
import com.panch.service.MenuService;
import com.panch.service.RoleService;
import com.panch.utils.BeanCopyUtils;
import com.panch.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LoginController {
    @Autowired
    private LoginService loginService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private LoginService loginServcie;

    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody User user){
        if(!StringUtils.hasText(user.getUserName())){
            //提示 必须要传用户名
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return loginService.login(user);
    }

    @PostMapping("/user/logout")
    public ResponseResult logout(){
        return loginServcie.logout();
    }

    @GetMapping("getInfo")
    public ResponseResult<AdminUserInfoVo> getInfo(){
        //获取当前登录的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        //根据用户id查询权限信息
        List<String> perms = menuService.selectPermsByUserId(loginUser.getUser().getId());
        //根据用户id查询角色信息
        List<String> roleKeyList = roleService.selectRoleKeyByUserId(loginUser.getUser().getId());

        //获取用户信息
        User user = loginUser.getUser();
        UserInfo userInfoVo = BeanCopyUtils.copyBean(user, UserInfo.class);
        //封装数据返回

        AdminUserInfoVo adminUserInfoVo = new AdminUserInfoVo(perms,roleKeyList,userInfoVo);
        return ResponseResult.okResult(adminUserInfoVo);
    }

    @GetMapping("getRouters")
    public ResponseResult<RoutersVo> getRouters(){
        Long userId = SecurityUtils.getUserId();
        //查询menu 结果是tree的形式
        List<Menu> menus = menuService.selectRouterMenuTreeByUserId(userId);
        //封装数据返回
//        List<MenuVo> menuVo = BeanCopyUtils.copyBeanList(menus, MenuVo.class);
        return ResponseResult.okResult(new RoutersVo(menus));
    }

}