package cn.zzz.bos.web.action.base;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
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

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import cn.zzz.bos.domain.base.Standard;
import cn.zzz.bos.service.base.StandardService;
import cn.zzz.bos.web.action.CommonAction;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**  
 * ClassName:StandardAction <br/>  
 * Function:  <br/>  
 * Date:     Jan 15, 2018 9:20:00 AM <br/>       
 */
//需要特别注意,本项目使用了Struts2的注解,注解要求Action的包名中必须包含action,actions,struts,struts2中的一种,如果没有框架将不会扫描包下的Action
@Controller
@Scope("prototype")      // 等价于applicationContext.xml中scope属性
@Namespace("/")          // 等价于struts.xml中package节点中namespace属性
@ParentPackage("struts-default")    // 等价于struts.xml中package节点中extends属性
public class StandardAction extends CommonAction<Standard> {
    
    public StandardAction() {
          
        super(Standard.class);  
        
    }

    private static final long serialVersionUID = 1L;
    
    @Autowired
    private StandardService standardService;
    
    //保存的方法
    // Action中的value等价于以前struts.xml中<action>节点的name
    // Result中的name等价于以前struts.xml中<result>节点的name
    // Result中的location等价于以前struts.xml中<result>节点之间的内容
    // Result中的type等价于以前请求回页面的类型，请求转发重定向
    @Action(value="standardAction_save",results={
            @Result(name="success",location="/pages/base/standard.html",type="redirect")})
    public String save(){
        
        standardService.save(getModel());
        
        return SUCCESS;
        
    }
    
    //分页查询数据
    @Action(value="standardAction_pageQuery")
    public String pageQuery() throws Exception{
        
        //JPA提供的Pageable，类似与PageBean对象，用于处理分页查询
        //easyUI传过来的page是从1开始的，而PageRequest是从0开始的，所以注意一定要减1
        Pageable pageable = new PageRequest(page - 1, rows);
        
        //调用service层的方法进行查询
        Page<Standard> page = standardService.findAll(pageable);
        
        page2json(page, null);
        
        return NONE;
        
    }
    
    //查询所有数据，提供数据给快递员的下拉选框使用
    @Action(value="standardAction_findAll")
    public String findAll() throws Exception{
        
        //调用service层的方法进行查询，把null传过去相当于调用其dao层的findAll()方法，它会查询该类对应表中的所有数据
        Page<Standard> page = standardService.findAll(null);
        
        //从page中获取数据集合
        List<Standard> list = page.getContent();
        
        list2json(list, null);
        
        return NONE;
        
    }

}
  
