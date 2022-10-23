package com.atguigu.guli.service.cms.controller.api;


import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.cms.entity.Ad;
import com.atguigu.guli.service.cms.service.AdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 广告推荐 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2022-07-30
 */
@RestController

@RequestMapping("/api/cms/ad")
public class ApiAdController {
    @Autowired
    AdService adService;
    //查询首页热门数据的接口方法
    @GetMapping
    public R getAds(){
        /*
            由于首页热门数据有多种类型：
                返回一个List集合不合适
                    cms连接的ad表可以查询到首页banner图数据

                    远程访问edu的课程表 可以查询到热门的课程数据集合


                    远程访问edu的讲师表 可以查询到讲师热门数据

         */
        Map<String,List> map =  adService.getHotAds();
        return R.ok().data("map",map);//r.data.map.key
    }


}

