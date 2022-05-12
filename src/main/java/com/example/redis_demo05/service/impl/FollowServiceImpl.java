package com.example.redis_demo05.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.redis_demo05.entity.Follow;
import com.example.redis_demo05.mapper.FollowMapper;
import com.example.redis_demo05.service.IFollowService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@Service
public class FollowServiceImpl extends ServiceImpl<FollowMapper, Follow> implements IFollowService {

}
