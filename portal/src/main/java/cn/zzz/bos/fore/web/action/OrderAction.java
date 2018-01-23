package cn.zzz.bos.fore.web.action;

import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import cn.zzz.bos.domain.base.Area;
import cn.zzz.bos.domain.take_delivery.Order;

/**  
 * ClassName:OrderAction <br/>  
 * Function:  <br/>  
 * Date:     Jan 22, 2018 3:59:02 PM <br/>       
 */
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("struts-default")
public class OrderAction extends ActionSupport implements ModelDriven<Order>{
    
    private Order model = new Order();

    @Override
    public Order getModel() {
          
        return model;
        
    }
    
    // 属性驱动获取收件人和发件人详细地址信息
    private String sendAreaInfo;
    private String recAreaInfo;
    
    public void setSendAreaInfo(String sendAreaInfo) {
        this.sendAreaInfo = sendAreaInfo;
    }
    public void setRecAreaInfo(String recAreaInfo) {
        this.recAreaInfo = recAreaInfo;
    }
    
    //保存订单
    @Action(value="orderAction_add")
    public String add(){
        
        // 如果发件人详细信息存在,将其转换为Area对象
        if(StringUtils.isNotEmpty(sendAreaInfo)){
            System.out.println(sendAreaInfo);
            String[] split = sendAreaInfo.split("/");
            Area sendArea = new Area(split[0],split[1],split[2]);
            model.setSendArea(sendArea);
        }
        
        // 如果收件人详细信息存在,将其转换为Area对象
        if(StringUtils.isNotEmpty(recAreaInfo)){
            String[] split = recAreaInfo.split("/");
            Area recArea = new Area(split[0],split[1],split[2]);
            model.setRecArea(recArea);
        }
        
        //调用webService的方法用WebClient保存订单
        WebClient
                .create("http://localhost:8080/bos_management_web/webService/orderService/save")
                .type(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .post(model);
        
        return NONE;
        
    }

}
  
