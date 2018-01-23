package cn.zzz.bos.service.base.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.zzz.bos.dao.base.StandardRepository;
import cn.zzz.bos.domain.base.Standard;
import cn.zzz.bos.service.base.StandardService;

/**  
 * ClassName:StandardServiceImpl <br/>  
 * Function:  <br/>  
 * Date:     Jan 15, 2018 9:27:36 AM <br/>       
 */
@Service
@Transactional
public class StandardServiceImpl implements StandardService {
    
    @Autowired
    private StandardRepository standardRepository;

    //保存Standard的方法
    @Override
    public void save(Standard standard) {

        //直接调用dao层的方法保存Standard
        standardRepository.save(standard);

    }

    //查询分页
    @Override
    public Page<Standard> findAll(Pageable pageable) {
          
        //直接调用dao层的方法查询
        return standardRepository.findAll(pageable);
        
    }

}
  
