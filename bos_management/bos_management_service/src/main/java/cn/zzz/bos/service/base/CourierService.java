package cn.zzz.bos.service.base;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import cn.zzz.bos.domain.base.Courier;

/**  
 * ClassName:CourierService <br/>  
 * Function:  <br/>  
 * Date:     Jan 15, 2018 5:54:50 PM <br/>       
 */
public interface CourierService {

    //保存快递员的方法
    void save(Courier courier);

    //查询所有
    Page<Courier> findAll(Pageable pageable);

    //作废快递员
    void deleteCourier(String ids);

    //分页查询
    Page<Courier> findAll(Specification<Courier> specification,Pageable pageable);

    //查询所有没有作废的快递员
    List<Courier> findUnDeltagCourier();

}
  
