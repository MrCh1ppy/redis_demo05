package com.example.redis_demo05.interceptor;

import com.example.redis_demo05.utils.RedisConstants;
import com.example.redis_demo05.utils.UserHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class LoginInterceptor implements HandlerInterceptor, RedisConstants {
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if(UserHolder.getUser()==null){
			response.setStatus(401);
			return false;
		}
		return true;
	}
}
