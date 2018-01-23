package cn.zzz.bos.dao.base;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import cn.zzz.bos.domain.base.Standard;

/**  
 * ClassName:StandardRepository <br/>  
 * Function:  <br/>  
 * Date:     Jan 14, 2018 4:15:28 PM <br/>       
 */
public interface StandardRepository extends JpaRepository<Standard, Long>{
    
    //自定义查询操作
    //自定义查询时,命名一定要符合规则
    
    //////////////////////////////标准命名方式///////////////////////////
    // 根据名字进行精准查询,Standard类中有name字段
    Standard findByName(String name);
    
    // 根据名字进行模糊查询
    List<Standard> findByNameLike(String name);
    
    // 查询最大重量为空的数据
    List<Standard> findByMaxWeightIsNull();
    
    // 多条件查询
    Standard findByIdAndName(Long id,String name);
    
    
    ////////////////////////////////自定义增删改操作/////////////////////////////////
    //根据name删除
    @Transactional
    @Modifying      //所有更细操作都要加这个注解
    @Query("delete from Standard where name = ?")
    void deleteByName(String name);
    
    //根据name修改
    @Transactional
    @Modifying
    @Query("update Standard set name = ? where name = ?")
    void updateByName(String updateName,String name);
    
    ////////////////////////////////////自定义查询操作/////////////////////////////////////
    //根据用户名和最大长度进行查询
    @Query("from Standard where name = ? and maxLength = ?")    //JPQL写法=====等价于HQL
    List<Standard> findByNameAndMaxLength(String name,Integer maxLength);
    
    // 根据用户名和最大长度 查询2
    @Query("from Standard where name = ?2 and maxLength = ?1")  //？一定要对应，不对应可以后面跟上数字对应起来
    List<Standard> findByNameAndMaxLength2(Integer maxLength,String name);
    
    //根据用户名和最大长度进行查询
    @Query(value = "select * from TT_STANDARD where C_NAME = ? and C_MAX_LENGTH = ?",nativeQuery = true)   //后面跟上nativeQuery = true：语句就是原生SQL
    List<Standard> findByNameAndMaxLength3(String name,Integer maxLength);

}
  
