package com.panch.filter;

import com.alibaba.fastjson.JSON;
import com.panch.config.RedisConfig;
import com.panch.config.SecurityConfig;
import com.panch.domain.LoginUser;
import com.panch.domain.ResponseResult;
import com.panch.enums.AppHttpCodeEnum;
import com.panch.utils.JwtUtil;
import com.panch.utils.RedisCache;
import com.panch.utils.WebUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Objects;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private RedisCache redisCache;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        //获取请求头的token
        String token = httpServletRequest.getHeader("token");
        //没有token，直接放行
        if (!StringUtils.hasText(token)){
            filterChain.doFilter(httpServletRequest,httpServletResponse);
            return;
        }
        //解析获得userid
        Claims claims = null;
        try {
            claims = JwtUtil.parseJWT(token);
        } catch (Exception e) {
            e.printStackTrace();
            //token超时或非法，给前端响应403
            ResponseResult responseResult = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
            //使用fastjson将对象转换为json字符串
            WebUtils.renderString(httpServletResponse, JSON.toJSONString(responseResult));
            return;
        }
        String userId = claims.getSubject();
        //从redis中获取用户信息
        LoginUser loginUser = redisCache.getCacheObject("bloglogin:" + userId);
       //如果没有redis，说明过期
        if(Objects.isNull(loginUser)){
            //token超时或非法，给前端响应403
            ResponseResult responseResult = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
            //使用fastjson将对象转换为json字符串
            WebUtils.renderString(httpServletResponse, JSON.toJSONString(responseResult));
            return;
        }

        //将用户信息放入SecurityContext中
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }
}
