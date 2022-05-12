package com.example.redis_demo05.interceptor;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.example.redis_demo05.dto.UserDTO;
import com.example.redis_demo05.utils.RedisConstants;
import com.example.redis_demo05.utils.UserHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
public class RefreshTokenInterceptor implements HandlerInterceptor, RedisConstants {
	private final StringRedisTemplate stringRedisTemplate;

	public RefreshTokenInterceptor(StringRedisTemplate stringRedisTemplate) {
		this.stringRedisTemplate = stringRedisTemplate;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String token = request.getHeader("authorization");
		if(token==null||token.isBlank()){
			return true;
		}
		Map<Object, Object> map = stringRedisTemplate.opsForHash().entries(token);
		if(map.isEmpty()){
			return true;
		}
		UserDTO dto = BeanUtil.mapToBean(map, UserDTO.class, false, CopyOptions.create()
				.setIgnoreError(false)
				.setIgnoreNullValue(true)
				.setFieldValueEditor((filedName,fieldValue)-> fieldValue.toString())
		);
		UserHolder.saveUser(dto);
		stringRedisTemplate.expire(token,LOGIN_TOKEN_TTL, TimeUnit.MINUTES);
		return true;
	}

}
