package com.atguigu.guli.service.edu.service;

import com.atguigu.guli.service.edu.entity.Chapter;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author atguigu
 * @since 2022-07-16
 */
public interface ChapterService extends IService<Chapter> {

    List<Chapter> getChaptersAndVideos(String courseId);
}
