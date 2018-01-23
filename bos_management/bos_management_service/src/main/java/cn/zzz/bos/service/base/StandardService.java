package cn.zzz.bos.service.base;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.zzz.bos.domain.base.Standard;

/**  
 * ClassName:StandardService <br/>  
 * Function:  <br/>  
 * Date:     Jan 15, 2018 9:25:50 AM <br/>       
 */
public interface StandardService {
    
    //保存Standard的方法
    void save(Standard standard);

    //查询分页
    Page<Standard> findAll(Pageable pageable);
    
}
  
