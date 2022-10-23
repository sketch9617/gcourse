package com.atguigu.guli.service.edu;

import com.atguigu.guli.service.base.result.R;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class RedisTest {

    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    RedisTemplate redisTemplate;
    @Test
    void testHash(){
        //存储删除失败的头像数据：
        /*
            teacher:delfail:avatars
                                url1   module1
                                url2  module2
         */
        //根据key获取redis中的hash结构
        BoundHashOperations<String, String, String> hashOps = stringRedisTemplate.boundHashOps("teacher:delfail:avatars");
        System.out.println("hash结构中的entry(k-v结构)的数量："+hashOps.size());

        hashOps.put("cuicui.jpg" , "avatar");
        hashOps.put("dongyu.jpg" , "avatar");
        hashOps.put("cc.jpg" , "avatar");
        hashOps.put("fangfang.jpg" , "avatar");
        hashOps.put("mengmeng.jpg" , "avatar");
        hashOps.put("yezi.jpg" , "avatar");
        System.out.println("hash结构中的entry(k-v结构)的数量："+hashOps.size());

        Map<String, String> entries = hashOps.entries();
        entries.forEach((k,v)->{
            System.out.println("key : "+k +", v :"+ v);
        });
        //获取key集合
        System.out.println(hashOps.keys());
        //获取value集合
        System.out.println(hashOps.values());
        //删除 key列表 的数据
        hashOps.delete("yezi.jpg","dongyu.jpg");

        System.out.println("hash结构中的entry(k-v结构)的数量："+hashOps.size());
    }
    @Test
    void testList(){
        // k --> List
        //使用key在redis中获取它对应的List集合对象
        BoundListOperations<String, String> listOps = stringRedisTemplate.boundListOps("listKey");
        // 如果集合不存在，BoundListOperations会初始化一个空的List集合对象
        System.out.println(listOps.size());
        //从左向集合中存入元素
        listOps.leftPush("hehe");
        listOps.leftPush("fangfang");
        listOps.leftPush("cc");
        listOps.leftPush("cuicui");
        listOps.leftPush("mengmeng");
        listOps.leftPush("dongyu");
        System.out.println(listOps.size());
        //查询指定索引位置的元素
        System.out.println(listOps.index(0));
        System.out.println(listOps.size());
        //从右消费元素(左进右出：消费最先进入的元素)
        System.out.println(listOps.rightPop());
        System.out.println(listOps.size());
    }



    @BeforeEach //在所有的单元测试方法执行之前执行
    void init(){
        //将键直接以字符串的方式来存储到redis中
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        //将值转为json字符串存储：将来读取数据时直接可以还原为对象
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
    }
    @Test
    void testRedisTemplate(){
//        redisTemplate.opsForValue().set("rtObj" , R.fail(),20,TimeUnit.MINUTES);

        Object rtObj = redisTemplate.opsForValue().get("rtObj");
        System.out.println(rtObj.getClass().getName());
        System.out.println(rtObj);
    }


    @Test
    void test(){
        stringRedisTemplate.opsForValue().set("srtKey","hehe");
        System.out.println("stringRedisTemplate.hasKey(\"srtKey\") = " + stringRedisTemplate.hasKey("srtKey"));
        stringRedisTemplate.opsForValue().set("srtKey2" , "haha",10, TimeUnit.MINUTES);//设置过期时间
        System.out.println("stringRedisTemplate.getExpire(\"srtKey2\",TimeUnit.SECONDS) = " + stringRedisTemplate.getExpire("srtKey2", TimeUnit.SECONDS));
        Gson gson = new Gson();
        stringRedisTemplate.opsForValue().set("srtObj" , gson.toJson(R.ok()));

        R r2 = gson.fromJson(stringRedisTemplate.opsForValue().get("srtObj"), R.class);
        System.out.println(r2);
        System.out.println("=======================================================");
        //redisTemplate保存的对象如果是系统类对象，系统类已经实现了序列化接口
        // 存入的对象在redis中不可读
        //如果我们希望redisTemplate能够像StringRedisTemplate一样自动将对象转为json字符串自动存到redis，读取时自动将字符串转为java对象
        //redisTemplate:是以序列化的形式将对象存到redis，需要自定义类型的对象实现序列化接口
        redisTemplate.opsForValue().set("rtObj" , R.fail(),20,TimeUnit.MINUTES);
        Object obj = redisTemplate.opsForValue().get("rtObj");
        System.out.println(obj);
        System.out.println(obj.getClass().getName());


    }

}
