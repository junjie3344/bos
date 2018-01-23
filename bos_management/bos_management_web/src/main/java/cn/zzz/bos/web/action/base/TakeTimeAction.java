package cn.zzz.bos.web.action.base;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.zzz.bos.domain.base.TakeTime;
import cn.zzz.bos.service.base.TakeTimeService;
import cn.zzz.bos.web.action.CommonAction;

/**  
 * ClassName:TakeTimeAction <br/>  
 * Function:  <br/>  
 * Date:     Jan 21, 2018 3:18:12 PM <br/>       
 */
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("struts-default")
public class TakeTimeAction extends CommonAction<TakeTime>{

    private static final long serialVersionUID = 1L;
    
    public TakeTimeAction() {
        
        super(TakeTime.class);  
        
    }
    
    @Autowired
    private TakeTimeService takeTimeService;
    
    //查询所有收派时间
    @Action(value="takeTimeAction_findAll")
    public String findAll() throws Exception{
        
        //直接调用service层的方法查询所有收派时间
        List<TakeTime> list = takeTimeService.findAll();
        
        //调用父类的方法将list转换成json字符串并写回页面
        list2json(list, null);
        
        return NONE;
        
    }

}
  
