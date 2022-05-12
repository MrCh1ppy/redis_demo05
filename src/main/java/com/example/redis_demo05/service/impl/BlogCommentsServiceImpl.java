package com.example.redis_demo05.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.redis_demo05.entity.BlogComments;
import com.example.redis_demo05.mapper.BlogCommentsMapper;
import com.example.redis_demo05.service.IBlogCommentsService;
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
public class BlogCommentsServiceImpl extends ServiceImpl<BlogCommentsMapper, BlogComments> implements IBlogCommentsService {

}
