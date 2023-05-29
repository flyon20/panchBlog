package com.panch.handler;

import com.alibaba.fastjson.JSON;
import com.panch.domain.ResponseResult;
import com.panch.enums.AppHttpCodeEnum;
import com.panch.utils.WebUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//授权失败处理器
@Component
public class AccessDeniedHandlerImpl implements org.springframework.security.web.access.AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        //打印异常信息
        accessDeniedException.printStackTrace();
        //返回json形式的错误信息403
        ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.NO_OPERATOR_AUTH);
        //使用fastjson将对象转换为json字符串
        WebUtils.renderString(httpServletResponse, JSON.toJSONString(result));
    }
}
