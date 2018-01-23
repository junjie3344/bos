package cn.zzz.bos.service.base;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.zzz.bos.domain.base.FixedArea;

/**  
 * ClassName:FixedAreaService <br/>  
 * Function:  <br/>  
 * Date:     Jan 19, 2018 4:45:36 PM <br/>       
 */
public interface FixedAreaService {

    //保存定区的方法
    void save(FixedArea fixedArea);

    //分页查询
    Page<FixedArea> findAll(Pageable pageable);

    //保存关联快递员信息
    void associationCourierToFixedArea(Long id, Long courierId, Long takeTimeId);

}
  
