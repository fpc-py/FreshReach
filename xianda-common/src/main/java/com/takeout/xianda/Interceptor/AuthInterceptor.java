package com.takeout.xianda.Interceptor;

import com.takeout.xianda.result.Result;
import com.takeout.xianda.utils.JwtUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import com.alibaba.fastjson2.JSON;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private JwtUtil jwtUtil;

    public static final String TOKEN_BLACKLIST = "token_blacklist:";
    private static final String AUTHORIZATION_HEADER = "Authorization";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        response.setContentType("application/json;charset=UTF-8");
        String token = request.getHeader(AUTHORIZATION_HEADER);

        //无token直接拦截
        if (token ==null || token.trim().isEmpty()){
            Result<Object> fail = Result.error(401, "未登录，请先登录");
            response.getWriter().write(JSON.toJSONString(fail));
            return false;
        }


        //判断是否在注销黑名单
        if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(TOKEN_BLACKLIST + token))) {
            Result<Object> fail = Result.error(401, "登陆已失效，请重新登录");
            response.getWriter().write(JSON.toJSONString(fail));
            return false;

        }
        //校验jwt签名和过期时间
        if (!jwtUtil.verifyToken(token)) {
            Result<Object> fail = Result.error(401, "token无效或已过期");
            response.getWriter().write(JSON.toJSONString(fail));
            return false;
        }
        //把用户信息存入request,controller可获取
        Long userId = jwtUtil.getUserId(token);
        request.setAttribute("userId", userId);
        return true;
    }
}
