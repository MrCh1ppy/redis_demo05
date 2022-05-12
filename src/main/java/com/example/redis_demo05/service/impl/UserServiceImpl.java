package com.example.redis_demo05.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.redis_demo05.dto.LoginFormDTO;
import com.example.redis_demo05.dto.Result;
import com.example.redis_demo05.dto.UserDTO;
import com.example.redis_demo05.entity.User;
import com.example.redis_demo05.mapper.UserMapper;
import com.example.redis_demo05.service.IUserService;
import com.example.redis_demo05.utils.RedisConstants;
import com.example.redis_demo05.utils.RegexUtils;
import com.example.redis_demo05.utils.SystemConstants;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 服务实现类
 * </p>
 * @author Mr Chippy
 * @since 2021-12-22
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService, RedisConstants,SystemConstants {

	@Resource
	private StringRedisTemplate stringRedisTemplate;

	@Override
	public Result sendCode(String phone, HttpSession session) {
		//check phone number
		if (RegexUtils.isPhoneInvalid(phone)){
			//if not return exception
			return Result.fail("手机号错误");
		}
		//get check code
		String code = RandomUtil.randomNumbers(6);
		//save code and send
		stringRedisTemplate.opsForValue().set(LOGIN_CODE_KEY+phone,code,LOGIN_CODE_TTL, TimeUnit.MINUTES);
		log.debug("send success code is {}",code);
		return Result.ok();
	}

	@Override
	public Result login(LoginFormDTO loginForm, HttpSession session) {
		String phone = loginForm.getPhone();
		if(RegexUtils.isPhoneInvalid(phone)){
			return Result.fail("Invalid phone number");
		}
		String code = stringRedisTemplate.opsForValue().get(LOGIN_CODE_KEY + phone);
		String cacheCode = loginForm.getCode();
		if(!cacheCode.equals(code)){
			return Result.fail("error code");
		}
		User user = query().eq("phone", phone).one();
		if(user==null){
			createUserWithPhone(phone);
		}
		UserDTO dto = BeanUtil.copyProperties(user, UserDTO.class);
		String uuid = UUID.randomUUID().toString(true);
		Map<String, Object> map = BeanUtil.beanToMap(dto,new HashMap<>(), CopyOptions.create().ignoreNullValue().setFieldValueEditor((a,b)->b.toString()));
		String key =  LOGIN_TOKEN_KEY+ uuid;
		stringRedisTemplate.opsForHash().putAll(key,map);
		stringRedisTemplate.expire(key,LOGIN_TOKEN_TTL,TimeUnit.MINUTES);
		return Result.ok(key);
	}

	private User createUserWithPhone(String phone){
		User user = new User(
				phone,
				"",
				USER_NICK_NAME_PREFIX +RandomUtil.randomString(6)
		);
		save(user);
		return user;
	}
}
