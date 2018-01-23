package cn.zzz.redis.test;

import java.util.concurrent.TimeUnit;

import javax.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.client.WebClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class RedisTest {
    
    @Autowired
    private RedisTemplate<String, String> template;
    
    @Test
    public void test01(){
        
        //添加数据
        template.opsForValue().set("name", "zhangsan");
        
    }
    
    @Test
    public void test02(){
        
        //删除数据
        template.delete("name");
        
    }
    
    @Test
    public void test03(){
        
        //添加数据，并指定过期时间
        /**
         * @param key
         * @param value
         * @param timeout : 超时时间
         * @param units : 时间单位
         */
        template.opsForValue().set("name", "lisi", 10, TimeUnit.SECONDS);
        
    }
    
    @Test
    public void test04(){
        
        WebClient
        .create("http://localhost:8180/crm/webService/customerService/active")
        .type(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .query("telephone", 15308991612L)
        .put(null);
        
    }

}
  
