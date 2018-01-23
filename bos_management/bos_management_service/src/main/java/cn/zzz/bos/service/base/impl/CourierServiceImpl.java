package cn.zzz.bos.service.base.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.zzz.bos.dao.base.CourierRepository;
import cn.zzz.bos.domain.base.Courier;
import cn.zzz.bos.service.base.CourierService;

/**  
 * ClassName:CourierServiceImpl <br/>  
 * Function:  <br/>  
 * Date:     Jan 15, 2018 5:55:22 PM <br/>       
 */
@Service
@Transactional
public class CourierServiceImpl implements CourierService {
    
    @Autowired
    private CourierRepository courierRepository;

    //保存快递员的方法
    @Override
    public void save(Courier courier) {
          
        //直接调用dao层的方法保存快递员
        courierRepository.save(courier);
        
    }

    //查询所有
    @Override
    public Page<Courier> findAll(Pageable pageable) {
        
        //直接调用dao层的方法查询所有
        return courierRepository.findAll(pageable);
        
    }

    //作废快递员
    @Override
    public void deleteCourier(String ids) {
        
        //先判断传进来的ids是否为空
        if(StringUtils.isNotEmpty(ids)){
            
            //不为空
            //切割数据取到所有id
            String[] split = ids.split(",");
            for (String id : split) {
                
                //执行删除，不要执行物理删除，执行作废，给其打个标志
                //所以需要自定义dao层的方法
                courierRepository.updateDelTagById(Long.parseLong(id));
                
            }
            
        }
          
        
    }

    //分页查询
    @Override
    public Page<Courier> findAll( Specification<Courier> specification,Pageable pageable) {
          
        return courierRepository.findAll(specification, pageable);
        
    }

    //查询所有没有作废的快递员
    @Override
    public List<Courier> findUnDeltagCourier() {
          
        return courierRepository.findByDeltagIsNull();
        
    }

}
  
