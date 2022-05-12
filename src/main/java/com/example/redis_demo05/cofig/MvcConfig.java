package com.example.redis_demo05.cofig;

import com.example.redis_demo05.interceptor.LoginInterceptor;
import com.example.redis_demo05.interceptor.RefreshTokenInterceptor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

	@Resource
	private StringRedisTemplate stringRedisTemplate;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new RefreshTokenInterceptor(stringRedisTemplate)).order(0);
		registry.addInterceptor(new LoginInterceptor())
				.addPathPatterns()
				.excludePathPatterns(
						"/user/login",
						"/shop/**",
						"/voucher/**",
						"/upload/**",
						"shop-type/**",
						"/blog/hot",
						"/user/code"
				).order(1);
	}
}
