package cn.zzz.bos.service.take_delivery;

import javax.jws.WebService;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import cn.zzz.bos.domain.take_delivery.Order;

/**  
 * ClassName:OrderService <br/>  
 * Function:  <br/>  
 * Date:     Jan 22, 2018 4:14:45 PM <br/>       
 */
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface OrderService {

    //保存订单
    @POST
    @Path("/save")
    void save(Order order);
    
}
  
