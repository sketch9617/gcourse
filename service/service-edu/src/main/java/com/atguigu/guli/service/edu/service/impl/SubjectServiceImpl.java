package com.atguigu.guli.service.edu.service.impl;

import com.alibaba.excel.EasyExcel;
import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.base.result.ResultCodeEnum;
import com.atguigu.guli.service.edu.entity.Subject;
import com.atguigu.guli.service.edu.entity.excel.SubjectExcelData;
import com.atguigu.guli.service.edu.listener.SubjectExcelDataListener;
import com.atguigu.guli.service.edu.mapper.SubjectMapper;
import com.atguigu.guli.service.edu.service.SubjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2022-07-16
 */
@Service
public class SubjectServiceImpl extends ServiceImpl<SubjectMapper, Subject> implements SubjectService {
    /*
        自动装配时，从容器中装配对象使用，装配的对象是由容器来初始化 并为对象设置了autowired属性值
        手动初始化对象时，成员属性需要我们自己初始化
     */
    //    @Autowired
//    SubjectExcelDataListener subjectExcelDataListener;
    @Override
    public void importSubjects(MultipartFile subjects) {

        try {
            EasyExcel.read(subjects.getInputStream())
                        .head(SubjectExcelData.class)
                        .registerReadListener(new SubjectExcelDataListener(baseMapper))
                        .doReadAll();
        } catch (Exception e) {
            throw new GuliException(ResultCodeEnum.EXCEL_DATA_IMPORT_ERROR , e);
        }

}

    @Override
    public List<Subject> getNestedSubjects() {
        //课件中此处是自定义sql： 连接查询 效率一般
        /*
            select ps.* , cs.*
            from edu_subject ps
            left join edu_subject cs
            on ps.id = cs.parent_id
            where ps.parent_id = '0'

            1个一级分类 -> 多个二级分类.
         */
        //查询整张表的所有数据
        List<Subject> subjects = this.list();
        //1、挑选出一级分类List集合:
        List<Subject> pSubjects = subjects.stream().filter(subject -> subject.getParentId().equals("0")).collect(Collectors.toList());
        //2、遍历一级分类 挑选出所有课程分类属于该一级分类的子分类设置到一级分类的children集合中
        pSubjects.forEach(pSubject->{
            List<Subject> children = subjects.stream().filter(subject -> subject.getParentId().equals(pSubject.getId())).collect(Collectors.toList());
            pSubject.setChildren(children);
        });
        return pSubjects;
    }
}
