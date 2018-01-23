package cn.zzz.bos.service.take_delivery.impl;

import org.springframework.beans.factory.annotation.Autowired;

import cn.zzz.bos.dao.take_devlivery.WorkBillRepositype;
import cn.zzz.bos.domain.take_delivery.WorkBill;
import cn.zzz.bos.service.take_delivery.WorkBillService;

/**  
 * ClassName:WorkBillServiceImpl <br/>  
 * Function:  <br/>  
 * Date:     Jan 22, 2018 7:11:34 PM <br/>       
 */
public class WorkBillServiceImpl implements WorkBillService {
    
    @Autowired
    private WorkBillRepositype workBillRepositype;

    //保存工单
    @Override
    public void save(WorkBill workBill) {
        
        //调用dao层的方法保存工单
        workBillRepositype.save(workBill);

    }

}
  
