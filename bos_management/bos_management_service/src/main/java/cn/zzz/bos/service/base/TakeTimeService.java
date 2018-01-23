package cn.zzz.bos.service.base;

import java.util.List;

import cn.zzz.bos.domain.base.TakeTime;

/**  
 * ClassName:TakeTimeService <br/>  
 * Function:  <br/>  
 * Date:     Jan 21, 2018 3:21:54 PM <br/>       
 */
public interface TakeTimeService {

    //查询所有收派时间
    List<TakeTime> findAll();

}
  
