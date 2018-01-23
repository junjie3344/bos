package cn.zzz.bos.service.base.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.zzz.bos.dao.base.TakeTimeRepository;
import cn.zzz.bos.domain.base.TakeTime;
import cn.zzz.bos.service.base.TakeTimeService;

/**  
 * ClassName:TakeTimeServiceImpl <br/>  
 * Function:  <br/>  
 * Date:     Jan 21, 2018 3:22:05 PM <br/>       
 */
@Service
@Transactional
public class TakeTimeServiceImpl implements TakeTimeService {
    
    @Autowired
    private TakeTimeRepository takeTimeRepository;

    //查询所有收派时间
    @Override
    public List<TakeTime> findAll() {
          
        //直接调用dao层的方法查询所有收派标准
        return takeTimeRepository.findAll();
        
    }

}
  
