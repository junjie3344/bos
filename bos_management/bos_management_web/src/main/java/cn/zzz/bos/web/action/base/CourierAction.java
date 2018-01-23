package cn.zzz.bos.web.action.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import cn.zzz.bos.domain.base.Courier;
import cn.zzz.bos.domain.base.Standard;
import cn.zzz.bos.service.base.CourierService;
import cn.zzz.bos.web.action.CommonAction;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**  
 * ClassName:CourierAction <br/>  
 * Function:  <br/>  
 * Date:     Jan 15, 2018 5:38:02 PM <br/>       
 */
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("struts-default")
public class CourierAction extends CommonAction<Courier> {
    
    public CourierAction() {
          
        super(Courier.class);  
        
    }

    private static final long serialVersionUID = 1L;

    @Autowired
    private CourierService courierService;
    
    //保存的方法
    @Action(value="courierAction_save",results={@Result(name="success",location="/pages/base/courier.html",type="redirect")})
    public String save(){
        
        //直接调用dao层的方法去保存快递员
        courierService.save(getModel());
        
        return SUCCESS;
        
    }
    
    
    //分页查询
   /* @Action(value="courierAction_pageQuery")
    public String pageQuery() throws Exception{
        
        //easyUI传过来的page是从1开始的，而PageRequest是从0开始的，所以注意一定要减1
        Pageable pageable = new PageRequest(page - 1, rows);
        
        //调用service层的方法进行查询
        Page<Courier> page = courierService.findAll(pageable);
        
        //从page中获取总记录条数
        long total = page.getTotalElements();
        
        //从page中获取数据集合
        List<Courier> list = page.getContent();
        
        //然后把数据封装到一个map对象中，将其转换成json字符串
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("total", total);
        map.put("rows", list);
        
        //调用net的方法将map转换成json字符串
        //JSONObject  转换对象和Map集合
        //JSONArray   转换数组和List集合
        
         * 解决懒加载的第三种办法
         *    转换成json字符串之前声明一个JsonConfig对象
         *    然后调用其方法setExcludes设置要忽略的字段,方法中放数组
         *    提高服务器的性能,所有页面不需要的数据一律要忽略
         * 
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[]{"fixedAreas","takeTime"});
        String json = JSONObject.fromObject(map,jsonConfig).toString();
        
        //将json数据写回页面
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(json);
        
        return NONE;
        
    }*/
    
    //改进分页查询
    @Action(value="courierAction_pageQuery")
    public String pageQuery() throws Exception{
        
        // 获取快递员编号
        final String courierNum = getModel().getCourierNum();
        
        // 获取收派标准,用于获取标准名字
        final Standard standard = getModel().getStandard();
        
        // 获取所属单位
        final String company = getModel().getCompany();
        
        // 获取类型
        final String type = getModel().getType();
        
        // 构造查询条件
        Specification<Courier> specification = new Specification<Courier>() {

            /**
             * 构建一个where条件语句
             * 
             * @param root : 通常指实体类,根对象,简单的理解为泛型的对象
             * @param query : 查询对象
             * @param cb : 构造查询条件的对象
             * @return 组合查询条件
             */
            @Override
            public Predicate toPredicate(Root<Courier> root, CriteriaQuery<?> query,
                    CriteriaBuilder cb) {
                
                // 这个集合用来装载多个查询条件
                List<Predicate> list = new ArrayList<>();
                
                // 如果快递员编号不为空,构造等值查询
                // where courierNum = ?
                if(StringUtils.isNotEmpty(courierNum)){
                    // 参数1 : courierNum
                    // 参数2 : 代替?的具体值
                    Predicate p1 = cb.equal(root.get("courierNum").as(String.class), courierNum);
                    list.add(p1);
                }
                
                // 如果所属单位不为空,构造模糊查询
                if(StringUtils.isNotEmpty(company)){
                    Predicate p2 = cb.like(root.get("company").as(String.class), "%" + company + "%");
                    list.add(p2);
                }
                
                // 如果类型不为空,构造等值查询
                if(StringUtils.isNotEmpty(type)){
                    Predicate p3 = cb.equal(root.get("type").as(String.class), type);
                    list.add(p3);
                }
                
                // 如果收派标准不为空,构造等值查询
                if(standard != null && StringUtils.isNotEmpty(standard.getName())){
                    // 根据收派标准的名字进行级联查询
                    Join<Object, Object> join = root.join("standard");
                    Predicate p4 = cb.equal(join.get("name").as(String.class), standard.getName());
                    list.add(p4);
                }
                
                // 如果集合size为空,return null
                if(list.size() == 0){
                    return null;
                }
                
                // 把集合对象转换为数组
                Predicate[] array = new Predicate[list.size()];
                list.toArray(array);
                // 构造查询条件
                return cb.and(array);
                
            }
            
        };
        
        // PageRequest的页码下标从0开始,所以要-1
        Pageable pageable = new PageRequest(page - 1, rows);
        
        // 查询数据
        Page<Courier> page = courierService.findAll(specification,pageable);
        
        // 把数据转化为json字符串
        JsonConfig jsonConfig = new JsonConfig();
        // 设置序列化时忽略的字段
        jsonConfig.setExcludes(new String[] {"fixedAreas","takeTime"});
        
        page2json(page, jsonConfig);
        
        return NONE;
        
    }
    
    //获取所有要作废的ID
    private String ids;
    
    //作废快递员
    @Action(value="courierAction_deleteCourier",results={@Result(name = "success",location = "/pages/base/courier.html",type = "redirect")})
    public String deleteCourier(){
        
        //直接调用service层的方法作废快递员
        courierService.deleteCourier(ids);
        
        return SUCCESS;
        
    }

    public void setIds(String ids) {
        this.ids = ids;
    }
    
    //*****************************用于定区关联快递员**************************************
    
    //查询所有没有作废的快递员  即查询属性deltag为null的快递员
    @Action(value="courierAction_unDeltagCourier")
    public String unDeltagCourier() throws Exception{
        
        /*方法一*/
        Specification<Courier> specification = new Specification<Courier>() {

            @Override
            public Predicate toPredicate(Root<Courier> root, CriteriaQuery<?> query,
                    CriteriaBuilder cb) {
                
                Predicate predicate = cb.isNull(root.get("deltag").as(Character.class));
                return predicate;
                
            }
        };
        
        //调用service层的方法查询未作废的快递员
        Page<Courier> page = courierService.findAll(specification, null);
        
        List<Courier> list = page.getContent();
        
        /*方法二*/
        //调用service层的方法去查询未作废的快递员
        //List<Courier> list = courierService.findUnDeltagCourier();
        
        //设置忽略的属性
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[] {"fixedAreas","takeTime"});
        
        //调用父类的方法将其转化成json字符串并写回页面
        list2json(list, jsonConfig);
        
        return NONE;
        
    }
    
    //*****************************用于定区关联快递员**************************************

}
  
