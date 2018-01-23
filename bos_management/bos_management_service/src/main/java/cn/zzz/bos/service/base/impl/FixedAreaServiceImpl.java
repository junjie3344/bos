package cn.zzz.bos.service.base.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.zzz.bos.dao.base.CourierRepository;
import cn.zzz.bos.dao.base.FixedAreaRepository;
import cn.zzz.bos.dao.base.TakeTimeRepository;
import cn.zzz.bos.domain.base.Courier;
import cn.zzz.bos.domain.base.FixedArea;
import cn.zzz.bos.domain.base.TakeTime;
import cn.zzz.bos.service.base.FixedAreaService;

/**  
 * ClassName:FixedAreaServiceImpl <br/>  
 * Function:  <br/>  
 * Date:     Jan 19, 2018 4:45:54 PM <br/>       
 */
@Service
@Transactional
public class FixedAreaServiceImpl implements FixedAreaService {
    
    @Autowired
    private FixedAreaRepository fixedAreaRepository;
    @Autowired
    private CourierRepository courierRepository;
    @Autowired
    private TakeTimeRepository takeTimeRepository;

    //保存定区的方法
    @Override
    public void save(FixedArea fixedArea) {
        
        //直接调用dao层的方法保存定区
        fixedAreaRepository.save(fixedArea);
        
    }

    //分页查询
    @Override
    public Page<FixedArea> findAll(Pageable pageable) {
          
        //直接调dao层的方法去查询所有
        return fixedAreaRepository.findAll(pageable);
        
    }

    //保存关联快递员信息
    @Override
    public void associationCourierToFixedArea(Long id, Long courierId, Long takeTimeId) {
          
        // 持久态的对象
        //调用dao层的方法去查询各自的持久态对象
        FixedArea fixedArea = fixedAreaRepository.findOne(id);
        Courier courier = courierRepository.findOne(courierId);
        TakeTime takeTime = takeTimeRepository.findOne(takeTimeId);
        
        //建立快递员和时间的关联
        courier.setTakeTime(takeTime);
        
        //建立快递员和定区的关联
        //在建立关联的时候只能使用定区关联快递员，不能使用快递员关联定区
        //原因是因为Courier类中fixedArea属性使用了mappedBy属性，代表快递员放弃了对中间表的维护
        fixedArea.getCouriers().add(courier);
        
    }

}
  
