package com.atguigu.guli.service.cms;

import com.atguigu.guli.service.cms.utils.HttpUtils;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class CmsTest {

    public static void main(String[] args) {
//        //短信平台的服务器地址：
//        String host = "http://dingxin.market.alicloudapi.com";
//        String path = "/dx/sendSms";//短信平台发送短信的接口
//        String method = "POST";
//        String appcode = "8e45699f00ff4bdbb807664045acd813";//短信平台的一个用户的唯一标志（购买的短信平台分配的唯一的id）
//        /*
//        400代表请求报文 参数错误：
//         */
//        // 请求报文和响应报文的头不支持中文
//        //1、请求头参数的map
//        Map<String, String> headers = new HashMap<String, String>();
//        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
//        headers.put("Authorization", "APPCODE " + appcode);
//        //2、请求参数的map：url?mobile=159xxxx9999
//        Map<String, String> querys = new HashMap<String, String>();
//        querys.put("mobile", "18516617511");//手机号
//        querys.put("param", "code:654321");//验证码
//        querys.put("tpl_id", "TP1711063");//短信模板id
//        //3、请求体参数的map： 转为json存到请求体中
//        Map<String, String> bodys = new HashMap<String, String>();
//        try {
//            /**
//             * 重要提示如下:
//             * HttpUtils请从
//             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
//             * 下载
//             *
//             * 相应的依赖请参照
//             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
//             */
//            //发起请求得到响应结果
//            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
//            //获取response的body：解析响应体输出
//            //{"return_code":"00000","order_id":"ALY1659320736624527348"}
//            // returncode为五个0代表成功 其他全部代表失败
//            System.out.println(EntityUtils.toString(response.getEntity()));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }


    @Test
    public void genCode() {

        String prefix = "";//用来拼接连接数据库的url地址数据库名称的、根据实际修改
        String moduleName = "cms";//用来拼接包名，当前模块名
        // 1、创建代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 2、全局配置
        GlobalConfig gc = new GlobalConfig();
        //System.getProperty("user.dir") == D:\ideaworkspace\xa20220310\guli-parent\service\service-edu
        String projectPath = System.getProperty("user.dir");//工作所在的项目路径，可以使用绝对路径
        gc.setOutputDir(projectPath + "/src/main/java");//逆向生成的文件输出的路径
        gc.setAuthor("atguigu");
        gc.setOpen(false); //生成后是否打开资源管理器
//        gc.setFileOverride(true); //重新生成时文件是否覆盖
        gc.setServiceName("%sService");//默认Service接口会使用I开头，去掉首字母I
        gc.setIdType(IdType.ASSIGN_ID); //主键策略 使用雪花算法
        gc.setDateType(DateType.ONLY_DATE);//定义生成的实体类中日期类型使用java.util.Date
        gc.setSwagger2(true);//开启Swagger2模式:自动生成swagger注解
        mpg.setGlobalConfig(gc);

        // 3、数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        //jdbc:mysql://192.168.1.170:3306/guli_edu
        dsc.setUrl("jdbc:mysql://192.168.1.170:3306/" + prefix + "guli_" + moduleName + "?serverTimezone=GMT%2B8");
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("123456");
        dsc.setDbType(DbType.MYSQL);//数据库类型
        mpg.setDataSource(dsc);

        // 4、包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent("com.atguigu.guli.service");
        pc.setModuleName(moduleName); //模块名
        pc.setController("controller");
        pc.setEntity("entity");
        pc.setService("service");
        pc.setMapper("mapper");
        mpg.setPackageInfo(pc);

        // 5、策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);//数据库表映射到实体的命名策略
        strategy.setTablePrefix(moduleName + "_");//设置表前缀不生成  edu_teacher->  Teacher

        strategy.setColumnNaming(NamingStrategy.underline_to_camel);//数据库表字段映射到实体的命名策略
        strategy.setEntityLombokModel(true); // lombok 模型 @Accessors(chain = true) setter链式操作

        strategy.setLogicDeleteFieldName("is_deleted");//逻辑删除字段名  会自动添加@TableLogic注解
        strategy.setEntityBooleanColumnRemoveIsPrefix(true);//去掉布尔值的is_前缀

        //自动填充
        TableFill gmtCreate = new TableFill("gmt_create", FieldFill.INSERT);
        TableFill gmtModified = new TableFill("gmt_modified", FieldFill.INSERT_UPDATE);
        ArrayList<TableFill> tableFills = new ArrayList<>();
        tableFills.add(gmtCreate);
        tableFills.add(gmtModified);
        strategy.setTableFillList(tableFills);

        strategy.setRestControllerStyle(true); //restful api风格控制器
        strategy.setControllerMappingHyphenStyle(true); //url中驼峰转连字符  edu_teacher表的controller映射路径为 edu/teacher
        //设置BaseEntity: 所有的表都有新增和更新时间以及id字段，可以抽取到父类中管理
        strategy.setSuperEntityClass("com.atguigu.guli.service.base.model.BaseEntity");
        // 填写BaseEntity中的公共字段
        strategy.setSuperEntityColumns("id", "gmt_create", "gmt_modified");
        mpg.setStrategy(strategy);

        // 6、执行
        mpg.execute();
    }

}
