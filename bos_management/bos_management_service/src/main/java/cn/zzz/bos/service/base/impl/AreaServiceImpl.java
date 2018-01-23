package cn.zzz.bos.service.base.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.zzz.bos.dao.base.AreaRepository;
import cn.zzz.bos.domain.base.Area;
import cn.zzz.bos.service.base.AreaService;

/**  
 * ClassName:AreaServiceImpl <br/>  
 * Function:  <br/>  
 * Date:     Jan 17, 2018 5:30:14 PM <br/>       
 */
@Service
@Transactional
public class AreaServiceImpl implements AreaService {
    
    @Autowired
    private AreaRepository areaRepository;

    //保存上传文件的数据
    @Override
    public void save(List<Area> list) {
          
        areaRepository.save(list);
        
    }

    //分页查询
    @Override
    public Page<Area> findAll(Pageable pageable) {
          
        //直接调用dao层的方法查询分页
        return areaRepository.findAll(pageable);
        
    }

    //根据输入框输入的值模糊查询
    @Override
    public List<Area> findQ(String q) {
        
        //对q进行处理，如果输入是小写，就让其转化为大写   toUpperCase这个方法会自动识别，只有为小写的时候才会进行转化，其他不关影响
        q = "%" + q.toUpperCase() + "%";
          
        return areaRepository.findQ(q);
        
    }

}
  
