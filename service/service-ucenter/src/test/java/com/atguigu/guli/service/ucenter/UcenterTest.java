package com.atguigu.guli.service.ucenter;

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
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.DefaultClaims;
import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;

public class UcenterTest {
    /*
        base64:
            自己提供了一个码表：
                包含 a~z/A~Z 和0~9   以及 + /     =(保留字符) 组成的字符

            base64将要处理的字符串的字节数组，每三位分成1组，然后每组重新划分为4个字节,最后一组如果不足三位使用=补齐
            [1110 1010][0000 1001][1010 0101]
            每个字节只有6位，base64在每个字节的高位补充两个0，得到的每个字节最大值为0~63,最终可以对应base64码表中的一个字符
            [0011 1010] [0010 0000][0010 0110] [0010 0101]


            base64解码：
                将base64编码的字节数组 每4位分为一组
                [0011 1010] [0010 0000][0010 0110] [0010 0101]
                删除每一个字节高位的两个0
                [11 1010] [10 0000][10 0110] [10 0101]
                将4个字节重新合并为3个字节得到原本的数据的字节数组


            以后中文传递、前端移动端文件提交、加密结果避免乱码 都可以使用base64处理
     */
    @Test
    public void testBase64() throws UnsupportedEncodingException {
        //base64不是加密算法，对可能出现乱码的字符串编码处理 保证结果可读
        String str = "尚硅谷a1";
        //将中文字符串编码
        byte[] encodeStr = Base64.getEncoder().encode(str.getBytes("UTF-8"));
        System.out.println(new String(encodeStr));
        // 解码
        byte[] decodeStr = Base64.getDecoder().decode(encodeStr);
        System.out.println(new String(decodeStr,"UTF-8"));
    }
    @Test
    public  void testUrlEncoder() throws UnsupportedEncodingException {

        String url = "http://localhost:8110/edu/teacher?name=张三";
        String encodeUrl = URLEncoder.encode(url, "UTF-8");
        System.out.println(encodeUrl);

        String decodeUrl = URLDecoder.decode(url, "UTF-8");
        System.out.println(decodeUrl);
    }

    static long expire = 60*60*1000;
    static String key = "asd.123@";
    public static void main1(String[] args) {
        //构建一个jwt： 用户登录成功时根据查询到的用户信息创建jwt token
        String jwtToken = Jwts.builder()
                //1、jwt的头
                .setHeaderParam("typ", "jwt") //jwt的类型
                .setHeaderParam("alg", "HS256") // jwt的签名使用的算法
                //2、jwt的载荷
                //2.1 jwt定义的载荷
//                .claim("subject" ,"GULI-TOKEN") //jwt的主题：代表授权token的作用范围
//                .claim("expire" ,new Date(System.currentTimeMillis()+expire)) //jwt的过期时间
                .setSubject("GULI-TOKEN")
                .setExpiration(new Date(System.currentTimeMillis() + expire))
                //2.2 用户的数据: 一般不存放隐私数据(密码)
                .claim("id", "9528")
                .claim("avatar", "http://asdasd/ff.jpg")
                .claim("nickname", "ff")
                //3、jwt的签名
                .signWith(SignatureAlgorithm.HS256, key)
                //构建jwt的字符串
                .compact();

        System.out.println(jwtToken);

        //不使用秘钥解析jwt的载荷：
        // 只要遵循jwt的格式就可以将载荷解析出来，可以看到内部内容，如果希望不能被查看，可以自定义对载荷中的数据加密
        // jwt的载荷数据 通过签名保证不能修改




    }

    public static void main(String[] args) {
        /*
            jwt解析token时：
                1、如果过期了 认为jwt不能使用
                2、验证签名： 使用服务器中存储的秘钥对jwt中的有效载荷的数据用头中的加密方式再次生成签名
                            和jwt的签名对比 如果不一致认为数据或者签名被篡改
         */
        try {
            String token = "eyJ0eXAiOiJqd3QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJHVUxJLVRPS0VOIiwiZXhwIjoxNjU5NDA2OTM4LCJpZCI6Ijk1MjgiLCJhdmF0YXIiOiJodHRwOi8vYXNkYXNkL2ZmLmpwZyIsIm5pY2tuYW1lIjoiZmYifQ.tYa59jsPyRQGVTII6lJqbRP57O-KLYJLguJ9pVTczBc";
            //解析token
            Jwt jwt = Jwts.parser()
                    .setSigningKey(key) // 设置秘钥
                    .parse(token);

            Header header = jwt.getHeader();
            System.out.println(header);
            DefaultClaims body = (DefaultClaims) jwt.getBody();
//            System.out.println(body.getClass().getName());
            System.out.println(body.get("id"));
            System.out.println(body.getSubject());
            System.out.println(body.getExpiration());
            System.out.println(body.get("nickname"));
            System.out.println(body.get("avatar"));
        } catch (ExpiredJwtException e) {
//            e.printStackTrace();
            System.out.println("jwt token已过期...");
        } catch(SignatureException e){
            System.out.println("jwt 被篡改 请重新登录...");
        }
    }


    @Test
    public void genCode() {

        String prefix = "";//用来拼接连接数据库的url地址数据库名称的、根据实际修改
        String moduleName = "ucenter";//用来拼接包名，当前模块名
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
