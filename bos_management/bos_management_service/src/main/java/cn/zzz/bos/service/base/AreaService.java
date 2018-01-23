package cn.zzz.bos.service.base;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.zzz.bos.domain.base.Area;

/**  
 * ClassName:AreaService <br/>  
 * Function:  <br/>  
 * Date:     Jan 17, 2018 5:29:46 PM <br/>       
 */
public interface AreaService {

    //保存导入文件的数据
    void save(List<Area> list);

    //分页查询
    Page<Area> findAll(Pageable pageable);

    //根据输入框输入的值模糊查询
    List<Area> findQ(String q);

}
  
