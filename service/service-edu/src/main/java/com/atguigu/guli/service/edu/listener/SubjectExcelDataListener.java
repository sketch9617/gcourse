package com.atguigu.guli.service.edu.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.atguigu.guli.service.edu.entity.Subject;
import com.atguigu.guli.service.edu.entity.excel.SubjectExcelData;
import com.atguigu.guli.service.edu.mapper.SubjectMapper;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//@Component
public class SubjectExcelDataListener extends AnalysisEventListener<SubjectExcelData> {
//    @Autowired
    SubjectMapper subjectMapper;

    public SubjectExcelDataListener(SubjectMapper baseMapper) {
        this.subjectMapper = baseMapper;
    }

    @Override
    public void invoke(SubjectExcelData subjectExcelData, AnalysisContext analysisContext) {
        //课程分类数据较少，读一行存一行
        String levelOneSubjectTitle = subjectExcelData.getLevelOneSubjectTitle();
        String levelTwoSubjectTitle = subjectExcelData.getLevelTwoSubjectTitle();

        //保存一级和二级分类数据到课程分类表
        //1、如果一级分类不存在，新增
        //2、如果已存在 查询
        LambdaQueryWrapper<Subject> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Subject::getTitle , levelOneSubjectTitle);
        lambdaQueryWrapper.eq(Subject::getParentId , "0");//表示该分类是1级分类
        Subject parentSubject = subjectMapper.selectOne(lambdaQueryWrapper);
        if(parentSubject==null){//数据库中不存在 一级分类 需要新增
            parentSubject = new Subject();
            parentSubject.setTitle(levelOneSubjectTitle);
            parentSubject.setSort(0);
            parentSubject.setParentId("0");
            subjectMapper.insert(parentSubject);//存入成功会自动返回一级分类的id设置给parentSubject
        }
        String subjectId = parentSubject.getId();//获取一级分类的id
        //3、如果二级分类不存在  新增(一定要绑定它的父分类的id)
        lambdaQueryWrapper.clear();//清空之前缓存的条件
        lambdaQueryWrapper.eq(Subject::getTitle , levelTwoSubjectTitle);
        lambdaQueryWrapper.eq(Subject::getParentId , subjectId);//查询上面的一级分类的二级分类对象
        Long count = subjectMapper.selectCount(lambdaQueryWrapper);
        if(count==0){//查询不到二级分类
            Subject subject = new Subject();
            subject.setTitle(levelTwoSubjectTitle);
            subject.setParentId(subjectId);
            subject.setSort(0);
            subjectMapper.insert(subject);
        }


    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
