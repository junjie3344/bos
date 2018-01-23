package cn.zzz.crm.service;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import cn.zzz.crm.domain.Customer;

/**  
 * ClassName:CustomerService <br/>  
 * Function:  <br/>  
 * Date:     Jan 20, 2018 3:14:40 PM <br/>       
 */
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface CustomerService {
    
    @GET
    @Path("/findAll")
    List<Customer> findAll();
    
    // 查找未关联定区的客户
    @GET
    @Path("/findCustomersUnAssociated")
    public List<Customer> findCustomersUnAssociated();
    
    // 查找关联到指定定区的客户
    @GET
    @Path("/findCustomersAssociated2FixedArea")
    public List<Customer> findCustomersAssociated2FixedArea(@QueryParam("fixedAreaId") String fixedAreaId);
    
    //保存关联客户信息
    @PUT
    @Path("/assignCustomers2FixedArea")
    public void assignCustomers2FixedArea(@QueryParam("fixedAreaId") String fixedAreaId,@QueryParam("customerIds") Long[] customerIds);
    
    //保存客户
    @POST
    @Path("/save")
    public void save(Customer customer);
    
    //根据手机号查找客户
    @GET
    @Path("/findByTelephone")
    public Customer findByTelephone(@QueryParam("telephone") String telephone);
    
    //激活用户
    @PUT
    @Path("/active")
    public void active(@QueryParam("telephone") String telephone);
    
    //登录
    @GET
    @Path("/login")
    public Customer login(@QueryParam("telephone") String telephone,@QueryParam("password") String password);
    
    // 根据地址查询定区ID
    @GET
    @Path("/findFixedAreaIdByAddress")
    String findFixedAreaIdByAddress(@QueryParam("address") String address);

}
  
