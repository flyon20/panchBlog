package com.panch.handler;

import com.alibaba.fastjson.JSON;
import com.panch.domain.ResponseResult;
import com.panch.enums.AppHttpCodeEnum;
import com.panch.utils.WebUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//认证失败处理器
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException authenticationException) throws IOException, ServletException {
        //打印异常信息
        authenticationException.printStackTrace();
        //返回json形式的错误信息503
        //ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_ERROR);
        //指定需要的异常处理信息
        ResponseResult result = null;
        if(authenticationException instanceof BadCredentialsException){
            //用户名或密码错误
            result = ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_ERROR.getCode(),authenticationException.getMessage());
        }else if(authenticationException instanceof InsufficientAuthenticationException){
            //缺少参数,提示用户输入验证码
            result = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }else{
            //其他错误，提示用户认证或授权失败，不提示具体原因，用户的锅
            result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR.getCode(),"认证或授权失败");
        }
        //使用fastjson将对象转换为json字符串
        WebUtils.renderString(httpServletResponse, JSON.toJSONString(result));
    }
}
