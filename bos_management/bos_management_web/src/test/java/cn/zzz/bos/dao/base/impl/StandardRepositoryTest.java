package cn.zzz.bos.dao.base.impl;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.zzz.bos.dao.base.StandardRepository;
import cn.zzz.bos.domain.base.Standard;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
/**  
 * ClassName:StandardRepositoryTest <br/>  
 * Function:  <br/>  
 * Date:     Jan 14, 2018 4:27:44 PM <br/>       
 */
public class StandardRepositoryTest {
    
    @Autowired
    private StandardRepository standardRepository;

    //测试增
    @Test
    public void addTest() {
        
        Standard standard = new Standard();
        standard.setName("李四");
        standard.setMaxLength(100);
        
        standardRepository.save(standard);
        
    }
    
    //测试删
    @Test
    public void deleteTest() {
        
        standardRepository.delete(-44L);
        
    }
    
    //测试改
    @Test
    public void updateTest() {
        
        //进行更改时，必须传入主键
        Standard standard = new Standard();
        standard.setId(-43L);
        standard.setName("李四四");
        standard.setMaxLength(100);
        
        standardRepository.save(standard);
        
    }
    
    //测试查询单个
    @Test
    public void findOneTest() {
        
        Standard standard = standardRepository.findOne(-43L);
        
        System.out.println(standard);
        
    }
    
    //测试查询所有
    @Test
    public void findAllTest() {
        
        List<Standard> list = standardRepository.findAll();
        
        for (Standard standard : list) {
            System.out.println(standard);
        }
        
    }
    
    /*测试查询操作*/
    /////////////////////////////////////////////////////////////
    //测试findByName
    @Test
    public void findByNameTest() {
        
        Standard standard = standardRepository.findByName("李四四");
        
        System.out.println(standard);
        
    }
    
    //测试findByNameLike
    @Test
    public void findByNameLike(){
        
        List<Standard> list = standardRepository.findByNameLike("%张三%");
        
        for (Standard standard : list) {
            System.out.println(standard);
        }
        
    }
    
    //测试findByMaxWeightIsNull
    @Test
    public void findByMaxWeightIsNull(){
        
        List<Standard> list = standardRepository.findByMaxWeightIsNull();
        
        for (Standard standard : list) {
            System.out.println(standard);
        }
        
    }
    
    //测试findByIdAndName
    @Test
    public void findByIdAndName(){
        
        Standard standard = standardRepository.findByIdAndName(1L, "张三");
        
        System.out.println(standard);
        
    }
    
    //测试deleteByName
    @Test
    public void deleteByName(){
        
        standardRepository.deleteByName("李四四");
        
    }
    
    //测试updateByName
    @Test
    public void updateByName(){
        
        standardRepository.updateByName("李四四", "李四");
        
    }
    
    //测试findByNameAndMaxLength
    @Test
    public void findByNameAndMaxLength(){
        
        List<Standard> list = standardRepository.findByNameAndMaxLength("李四四", 100);
        
        for (Standard standard : list) {
            System.out.println(standard);
        }
        
    }
    //测试findByNameAndMaxLength2
    @Test
    public void findByNameAndMaxLength2(){
        
        List<Standard> list = standardRepository.findByNameAndMaxLength2(100,"李四四");
        
        for (Standard standard : list) {
            System.out.println(standard);
        }
        
    }
    //测试findByNameAndMaxLength3
    @Test
    public void findByNameAndMaxLength3(){
        
        List<Standard> list = standardRepository.findByNameAndMaxLength3("李四四", 100);
        
        for (Standard standard : list) {
            System.out.println(standard);
        }
        
    }
    
    

}
  
