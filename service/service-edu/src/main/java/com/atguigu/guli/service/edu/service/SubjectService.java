package com.atguigu.guli.service.edu.service;

import com.atguigu.guli.service.edu.entity.Subject;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author atguigu
 * @since 2022-07-16
 */
public interface SubjectService extends IService<Subject> {

    void importSubjects(MultipartFile subjects);

    List<Subject> getNestedSubjects();

}
