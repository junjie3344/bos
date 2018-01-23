package cn.zzz.bos.web.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.springframework.data.domain.Page;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**  
 * ClassName:CommonAction <br/>  
 * Function:  <br/>  
 * Date:     Jan 17, 2018 8:57:07 PM <br/>       
 * @param <T>
 * 写一个父类的Action，抽取重复代码
 */
public class CommonAction<T> extends ActionSupport implements ModelDriven<T> {
    
    
    // ***********************************抽取getModel()功能  开始**************************************
    
    private T model;
    
    private Class<T> clazz;
    
    // 该构造函数用于初始化Model对象
    public CommonAction(Class<T> clazz){
        
        this.clazz = clazz;
        
        try {
            
            model = clazz.newInstance();
            
        } catch (Exception e) {
              
            e.printStackTrace();  
            
        }
        
    }

    @Override
    public T getModel() {
          
        return model;
        
    }
    
    // ********************************抽取getModel()功能  结束***************************************
    
    // *********************************抽取分页功能  开始***************************************
    
    //当前页码
    protected Integer page;
    
    //每页显示的数据大小
    protected Integer rows;
    
    //把Page对象转换成json字符串
    public void page2json(Page<T> page,JsonConfig jsonConfig) throws Exception{
        
        //获取总数据条数
        long total = page.getTotalElements();
        
        //获取所有数据
        List<T> rows = page.getContent();
        
        //封装数据
        Map<String, Object> map = new HashMap<>();
        map.put("total", total);
        map.put("rows", rows);
        
        //转化成json字符串
        // JSONObject 转换对象和Map集合
        // JSONArray 转换数组和List集合
        String json;
        if(jsonConfig != null){
            json = JSONObject.fromObject(map, jsonConfig).toString();
        }else {
            json = JSONObject.fromObject(map).toString();
        }
        
        //写回数据
        HttpServletResponse response = ServletActionContext.getResponse();
        //设置编码
        response.setContentType("application/json;charset=UTF-8");
        //写出
        response.getWriter().write(json);
        
    }
    
    //将list转换成json字符串
    public void list2json(List list,JsonConfig jsonConfig) throws Exception{
        
        String json;
        if(jsonConfig != null){
            json = JSONArray.fromObject(list, jsonConfig).toString();
        }else {
            json = JSONArray.fromObject(list).toString();
        }
        
        HttpServletResponse response = ServletActionContext.getResponse();
        //设置编码
        response.setContentType("application/json;charset=UTF-8");
        //写回数据
        response.getWriter().write(json);
        
    }
    
    
    public void setPage(Integer page) {
        this.page = page;
    }
    
    public void setRows(Integer rows) {
        this.rows = rows;
    }
    
    // *********************************抽取分页功能   结束***************************************

}
  
