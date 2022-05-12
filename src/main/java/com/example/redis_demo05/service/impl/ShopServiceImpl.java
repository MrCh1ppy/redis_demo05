package com.example.redis_demo05.service.impl;


import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.redis_demo05.dto.Result;
import com.example.redis_demo05.entity.Shop;
import com.example.redis_demo05.mapper.ShopMapper;
import com.example.redis_demo05.service.IShopService;
import com.example.redis_demo05.utils.RedisConstants;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@Service
public class ShopServiceImpl extends ServiceImpl<ShopMapper, Shop> implements IShopService, RedisConstants {

	@Resource
	private StringRedisTemplate stringRedisTemplate;

	@Override
	public Result queryById(Long id) {
		String key = CACHE_SHOP_KEY + id;
		String shopJson = stringRedisTemplate.opsForValue().get(key);
		if(CharSequenceUtil.isNotBlank(shopJson)){
			Shop shop = JSONUtil.toBean(shopJson, Shop.class);
			return Result.ok(shop);
		}
		if(shopJson!=""){
			return Result.fail("not exist");
		}
		Shop byId = getById(id);
		if(byId==null){
			return Result.ok("not exist");
		}
		stringRedisTemplate.opsForValue().set(key,JSONUtil.toJsonStr(byId),CACHE_SHOP_TTL, TimeUnit.MINUTES);
		return Result.ok(byId);
	}

	@Override
	public Result update(Shop shop) {
		updateById(shop);
		Long id = shop.getId();
		if(id==null){
			return Result.fail("empty id");
		}
		updateById(shop);
		stringRedisTemplate.delete(CACHE_SHOP_KEY+id);
		return Result.ok();
	}
}
