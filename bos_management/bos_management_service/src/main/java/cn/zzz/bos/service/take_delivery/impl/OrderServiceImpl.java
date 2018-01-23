package cn.zzz.bos.service.take_delivery.impl;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.zzz.bos.dao.base.AreaRepository;
import cn.zzz.bos.dao.base.FixedAreaRepository;
import cn.zzz.bos.dao.take_devlivery.OrderRepositype;
import cn.zzz.bos.dao.take_devlivery.WorkBillRepositype;
import cn.zzz.bos.domain.base.Area;
import cn.zzz.bos.domain.base.Courier;
import cn.zzz.bos.domain.base.FixedArea;
import cn.zzz.bos.domain.take_delivery.Order;
import cn.zzz.bos.domain.take_delivery.WorkBill;
import cn.zzz.bos.service.take_delivery.OrderService;
import cn.zzz.bos.utils.UUIDUtils;

/**  
 * ClassName:OrderServiceImpl <br/>  
 * Function:  <br/>  
 * Date:     Jan 22, 2018 4:16:04 PM <br/>       
 */
@Service
@Transactional
public class OrderServiceImpl implements OrderService {
    
    @Autowired
    private OrderRepositype orderRepositype;
    
    @Autowired
    private AreaRepository areaRepository;
    
    @Autowired
    private FixedAreaRepository fixedAreaRepository;
    
    @Autowired
    private WorkBillRepositype workBillRepositype;

   /* @Override
    public void save(Order order) {

        // 获取传递过来的发件地区对象,此时是游离态
        Area sendArea = order.getSendArea();
        if(sendArea != null){
            // 根据省市区查找持久态,并重新赋值给Order对象
            Area sendAreaDB = areaRepository.findByProvinceAndCityAndDistrict(sendArea.getProvince(), sendArea.getCity(), sendArea.getDistrict());
            order.setSendArea(sendAreaDB);
        }
        
        // 获取传递过来的收件地区对象,此时是游离态
        Area recArea = order.getRecArea();
        if(recArea != null){
            // 根据省市区查找持久态,并重新赋值给Order对象
            Area recAreaDB = areaRepository.findByProvinceAndCityAndDistrict(recArea.getProvince(), recArea.getCity(), recArea.getDistrict());
            order.setRecArea(recAreaDB);
        }
        
        //保存订单
        orderRepositype.save(order);

    }*/
    
    //保存订单，自动分单
    @Override
    public void save(Order order) {
          
        // 获取传递过来的发件地区对象,此时是游离态
        Area sendArea = order.getSendArea();
        if(sendArea != null){
            // 根据省市区查找持久态,并重新赋值给Order对象
            Area sendAreaDB = areaRepository.findByProvinceAndCityAndDistrict(sendArea.getProvince(), sendArea.getCity(), sendArea.getDistrict());
            order.setSendArea(sendAreaDB);
        }
        
        // 获取传递过来的收件地区对象,此时是游离态
        Area recArea = order.getRecArea();
        if(recArea != null){
            // 根据省市区查找持久态,并重新赋值给Order对象
            Area recAreaDB = areaRepository.findByProvinceAndCityAndDistrict(sendArea.getProvince(), sendArea.getCity(), sendArea.getDistrict());
            order.setRecArea(recAreaDB);
        }
        
        // 设置下单时间和订单号
        order.setOrderTime(new Date());
        order.setOrderNum(UUIDUtils.getId());
        
        // 保存订单
        orderRepositype.save(order);
        
        // 发件人地址不为空
        if(StringUtils.isNotEmpty(order.getSendAddress())){
            
            // 根据发件地址获取定区ID
            String fixedAreaID  = WebClient
                    .create("http://localhost:8180/crm/webService/customerService/findFixedAreaIdByAddress")
                    .type(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .query("address", order.getSendAddress())
                    .get(String.class);
            
            // 如果有定区ID
            if(StringUtils.isNotEmpty(fixedAreaID)){
                // 根据定区Id查找定区
                FixedArea fixedArea = fixedAreaRepository.findOne(Long.parseLong(fixedAreaID));
                // 获取定区关联的快递员
                Set<Courier> couriers = fixedArea.getCouriers();
                // 判断集合是否为空
                if(!couriers.isEmpty()){
                    Iterator<Courier> iterator = couriers.iterator();
                    // 找到第一个快递员(实际情况是找到此时正在上班的快递员)
                    Courier courier = iterator.next();
                    // 关联到订单
                    order.setCourier(courier);
                    // 生成工单
                    WorkBill workBill = new WorkBill();
                    workBill.setAttachbilltimes(0);
                    workBill.setBuildtime(new Date());
                    workBill.setCourier(courier);
                    workBill.setOrder(order);
                    workBill.setPickstate("新单");
                    workBill.setRemark(order.getRemark());
                    workBill.setSmsNumber("123");
                    workBill.setType("新");
                    //保存工单
                    workBillRepositype.save(workBill);
                    // 发送工单信息给快递员,此处打印日志进行模拟
                    System.out.println("工单信息:请到" + order.getSendAddress() + "取件,客户电话:"
                            + order.getSendMobile());
                    return;  
                }
            }
            
        }
        
    }

}
  
