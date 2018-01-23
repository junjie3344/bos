package cn.zzz.bos.dao.base;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.zzz.bos.domain.base.FixedArea;
import cn.zzz.bos.domain.base.SubArea;

/**  
 * ClassName:SubareaRepository <br/>  
 * Function:  <br/>  
 * Date:     Jan 18, 2018 5:22:44 PM <br/>       
 */
public interface SubareaRepository extends JpaRepository<SubArea, Long>,JpaSpecificationExecutor<SubArea>{

    //查询所有未关联定区的分区
    List<SubArea> findByFixedAreaIsNull();
    
    //根据定区id去查询已经关联的分区
    List<SubArea> findByFixedArea(FixedArea fixedArea);

    // 把关联到定区的客户全部解绑
    @Modifying
    @Query("update SubArea set fixedArea = null where fixedArea = ?1")
    void unbindByFixedAreaId(FixedArea fixedArea);

    // 再去进行绑定
    @Modifying
    @Query("update SubArea set fixedArea = ?1 where id = ?2")
    void bindFixedAreaById(FixedArea fixedArea, Long id);

    

}
  
