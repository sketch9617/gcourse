package com.atguigu.guli.service.cms.service.impl;

import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.base.result.ResultCodeEnum;
import com.atguigu.guli.service.cms.entity.Ad;
import com.atguigu.guli.service.cms.feign.EduFeignClient;
import com.atguigu.guli.service.cms.mapper.AdMapper;
import com.atguigu.guli.service.cms.service.AdService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 广告推荐 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2022-07-30
 */
@Service
public class AdServiceImpl extends ServiceImpl<AdMapper, Ad> implements AdService {
    @Autowired
    EduFeignClient eduFeignClient;
    @Autowired
    RedisTemplate redisTemplate;
    /*
        aop:
            每个需要缓存管理的业务方法 步骤都如下
                先查询缓存 有数据则返回
                没有数据  查询数据库 存到缓存中返回

         SpringCaching基于 aop实现了缓存管理：
            使用步骤：
                1、需要按照SpringCaching提供CacheManager接口编写 实现类(提供缓存管理的业务)
                    springboot-redis启动器已经提供了缓存管理的实现类
                2、需要在启动类上添加注解启用SpringCaching的缓存管理功能
                3、在需要缓存管理的查询方法添加缓存管理的注解(指定缓存的key)
     */
    //value+key组合形成 redis中缓存的键   拼接后的规则：  value::key
    @Cacheable(value = "ads",key = "'cache'")//key 的值 必须使用单引号引起
    @Override
    public Map<String, List> getHotAds() {
        //1、先判断redis中是否存在缓存
//        Object cacheObj = redisTemplate.opsForValue().get("ads:cache");
//        if(cacheObj!=null){
//            //有缓存
//            return (Map<String, List>) cacheObj;
//        }
        //2、没有缓存：查询数据库
        Map<String, List> map = new HashMap<>();
        //查询banner
        List<Ad> ads = this.list();
        map.put("banners" , ads);
        //查询热门课程
        R hotCoursesR = eduFeignClient.getHotCourses();
        if(!hotCoursesR.getSuccess()){
            throw new GuliException(ResultCodeEnum.SERVER_INNER_ERROR);
        }
        //查询热门讲师
        R hotTeachersR = eduFeignClient.getHotTeachers();
        if(!hotTeachersR.getSuccess()){
            throw new GuliException(ResultCodeEnum.SERVER_INNER_ERROR);
        }
        map.put("courses", (List) hotCoursesR.getData().get("items"));
        map.put("teachers", (List) hotTeachersR.getData().get("items"));
        //3、将数据存到缓存中: 增删改热门数据时 可以让缓存失效
//        redisTemplate.opsForValue().set("ads:cache",map,200 , TimeUnit.MINUTES);
        return map;
    }
}
