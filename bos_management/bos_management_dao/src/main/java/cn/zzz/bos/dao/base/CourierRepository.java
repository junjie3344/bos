package cn.zzz.bos.dao.base;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.zzz.bos.domain.base.Courier;

/**  
 * ClassName:CourierRepository <br/>  
 * Function:  <br/>  
 * Date:     Jan 15, 2018 5:48:21 PM <br/>       
 */
public interface CourierRepository extends JpaRepository<Courier, Long>,JpaSpecificationExecutor<Courier>{

    //作废的方法
    @Modifying
    @Query("update Courier set deltag = 1 where id = ?")
    void updateDelTagById(long parseLong);

    //查询所有没有作废的快递员
    List<Courier> findByDeltagIsNull();

}
  
