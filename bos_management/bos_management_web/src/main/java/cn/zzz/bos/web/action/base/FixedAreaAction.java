package cn.zzz.bos.web.action.base;

import java.util.Collection;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import cn.zzz.bos.domain.base.FixedArea;
import cn.zzz.bos.service.base.FixedAreaService;
import cn.zzz.bos.web.action.CommonAction;
import cn.zzz.crm.domain.Customer;
import net.sf.json.JsonConfig;

/**  
 * ClassName:FixedAreaAction <br/>  
 * Function:  <br/>  
 * Date:     Jan 19, 2018 4:41:13 PM <br/>       
 */
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("struts-default")
public class FixedAreaAction extends CommonAction<FixedArea>{

    private static final long serialVersionUID = 1L;

    public FixedAreaAction() {
          
        super(FixedArea.class);  
        
    }
    
    @Autowired
    private FixedAreaService fixedAreaService;
    
    //保存的方法
    @Action(value="fixedAreaAction_save",results={
            @Result(name="success",location="/pages/base/fixed_area.html",type="redirect")})
    public String save(){
        
        //直接调用service层的方法执行保存业务
        fixedAreaService.save(getModel());
        
        return SUCCESS;
        
    }
    
    //分页查询
    @Action(value="fixedAreaAction_pageQuery")
    public String pageQuery() throws Exception{
        
        //构建分页对象
        Pageable pageable = new PageRequest(page - 1, rows);
        
        //调用service的方法去处理查询业务
        Page<FixedArea> page = fixedAreaService.findAll(pageable);
        
        //设置忽略对象
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[] {"subareas","couriers"});
        
        //调用父类的方法转换成json字符串并写回
        page2json(page, jsonConfig);
        
        return NONE;
        
    }
    
    //********************************关联客户*******************************************
    
    //去向CRM发起请求,查询未关联的客户
    @Action(value="fixedAreaAction_findCustomersUnAssociated")
    public String findCustomersUnAssociated() throws Exception{
        
        //调用WebClient方法去CRM客户端请求查询未关联的客户
        List<Customer> list = (List<Customer>) WebClient
                .create("http://localhost:8180/crm/webService/customerService/findCustomersUnAssociated")
                .type(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .getCollection(Customer.class);
        
        //调用父类的方法将list转化成json字符串并写回数据
        list2json(list, null);
        
        return NONE;
        
    }
    
    //去向CRM发起请求,根据定区id查询已关联的客户
    @Action(value="fixedAreaAction_findCustomersAssociated2FixedArea")
    public String findCustomersAssociated2FixedArea() throws Exception{
        
        //调用WebClient方法去CRM客户端请求查询已关联的客户
        List<Customer> list = (List<Customer>) WebClient
                .create("http://localhost:8180/crm/webService/customerService/findCustomersAssociated2FixedArea")
                .type(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .query("fixedAreaId", getModel().getId())
                .getCollection(Customer.class);
        
        //调用父类的方法将list转化成json字符串并写回数据
        list2json(list, null);
        
        return NONE;
        
    }
    
    // 使用属性驱动获取被选中的客户的id
    private Long[] customerIds;
    
    public void setCustomerIds(Long[] customerIds) {
        this.customerIds = customerIds;
    }
    
    //保存关联客户信息
    @Action(value="fixedAreaAction_assignCustomers2FixedArea",results={
            @Result(name="success",location="/pages/base/fixed_area.html",type="redirect")})
    public String assignCustomers2FixedArea(){
        
        // 先把关联到定区ID的客户全部解绑
        // 再次进行绑定
        
        //调用WebClient方法去CRM客户端请求保存关联客户信息
        
        //判断customerIds是否有值，即右选框是否有值，有就传customerIds过去，没有就不传
        //如果传null过去会报错java.lang.IllegalArgumentException: name or values is null
        if(customerIds != null && customerIds.length > 0){
            WebClient
                    .create("http://localhost:8180/crm/webService/customerService/assignCustomers2FixedArea")
                    .type(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .query("fixedAreaId", getModel().getId())
                    .query("customerIds", customerIds)
                    .put(null);
        }else{
            WebClient
                    .create("http://localhost:8180/crm/webService/customerService/assignCustomers2FixedArea")
                    .type(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .query("fixedAreaId", getModel().getId())
                    .put(null);
        }
        
        
        return SUCCESS;
        
    }
    
    //********************************关联客户*******************************************
    
    //********************************关联快递员*****************************************
    
    //获取快递员id
    private Long courierId;
    //获取收派时间id
    private Long takeTimeId;
    
    public void setCourierId(Long courierId) {
        this.courierId = courierId;
    }
    
    public void setTakeTimeId(Long takeTimeId) {
        this.takeTimeId = takeTimeId;
    }
    
    //保存关联快递员信息
    @Action(value="fixedAreaAction_associationCourierToFixedArea",results={
            @Result(name="success",location="/pages/base/fixed_area.html",type="redirect")})
    public String associationCourierToFixedArea(){
        
        //调用service层的方法保存快递员信息
        fixedAreaService.associationCourierToFixedArea(getModel().getId(),courierId,takeTimeId);
        
        return SUCCESS;
        
    }
    
    //********************************关联快递员*****************************************

}
  
