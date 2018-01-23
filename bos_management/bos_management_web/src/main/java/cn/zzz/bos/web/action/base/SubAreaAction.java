package cn.zzz.bos.web.action.base;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
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

import cn.zzz.bos.domain.base.Area;
import cn.zzz.bos.domain.base.FixedArea;
import cn.zzz.bos.domain.base.SubArea;
import cn.zzz.bos.service.base.SubareaService;
import cn.zzz.bos.web.action.CommonAction;
import net.sf.json.JsonConfig;
import net.sf.json.util.PropertyFilter;

/**  
 * ClassName:SubareaAction <br/>  
 * Function:  <br/>  
 * Date:     Jan 18, 2018 5:19:18 PM <br/>       
 */
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("struts-default")
public class SubAreaAction extends CommonAction<SubArea>{

    private static final long serialVersionUID = 1L;

    public SubAreaAction() {
          
        super(SubArea.class);  
        
    }
    
    @Autowired
    private SubareaService subareaService;
    
    @Action(value="subareaAction_save",results={
            @Result(name="success",location="/pages/base/sub_area.html",type="redirect")})
    public String save(){
        
        subareaService.save(getModel());
        
        return SUCCESS;
        
    }
    
    //分页查询
    @Action(value="subareaAction_pageQuery")
    public String pageQuery() throws Exception{
        
        //获取关键字
        final String keyWords = getModel().getKeyWords();
        
        //获取区域,用于获取省，市，区
        final Area area = getModel().getArea();
        
        //获取定区，用于获取定区编码
        final FixedArea fixedArea = getModel().getFixedArea();
        
        //构造查询条件
        Specification<SubArea> specification = new Specification<SubArea>() {

            @Override
            public Predicate toPredicate(Root<SubArea> root, CriteriaQuery<?> query,
                    CriteriaBuilder cb) {
                
                List<Predicate> list = new ArrayList<>();
                
                //如果关键字不为空
                if(StringUtils.isNotEmpty(keyWords)){
                    Predicate p1 = cb.like(root.get("keyWords").as(String.class), "%" + keyWords + "%");
                    list.add(p1);
                }
                
                //如果区域不为空
                if(area != null){
                    //如果省份不为空
                    if(StringUtils.isNotEmpty(area.getProvince())){
                        Join<Object, Object> join = root.join("area");
                        Predicate p2 = cb.like(join.get("province").as(String.class), "%" + area.getProvince() + "%");
                        list.add(p2);
                    }
                    //如果市不为空
                    if(StringUtils.isNotEmpty(area.getCity())){
                        Join<Object, Object> join = root.join("area");
                        Predicate p3 = cb.like(join.get("city").as(String.class), "%" + area.getCity()  + "%");
                        list.add(p3);
                    }
                    //如果区域不为空
                    if(StringUtils.isNotEmpty(area.getDistrict())){
                        Join<Object, Object> join = root.join("area");
                        Predicate p4 = cb.like(join.get("district").as(String.class), "%" + area.getDistrict()  + "%");
                        list.add(p4);
                    }
                }
                
                //如果定区不为空
                /*if(fixedArea != null){
                    //如果id不为空
                    if(fixedArea != null){
                        Join<Object, Object> join = root.join("fixedArea");
                        Predicate p5 = cb.equal(join.get("id").as(Long.class), fixedArea.getId());
                        list.add(p5);
                    }
                }*/
                
                if(list.size() == 0){
                    return null;
                }
                
                Predicate[] array = new Predicate[list.size()];
                list.toArray(array);
                return cb.and(array);
                
            }};
        
        
        //构建分页对象
        Pageable pageable = new PageRequest(page - 1, rows);
        
        //调用service层的方法去处理查询
        Page<SubArea> page = subareaService.findAll(specification,pageable);
        
        // SubArea中包含Area,Area又包含SubArea,生成JSON时会循环查询,下面的代码禁止循环查询
        //方法1
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[]{"subareas"});
        //方法2
        /*jsonConfig.setJsonPropertyFilter(new PropertyFilter() {
            
            *//**
             * source 拥有属性的对象 <br>
             * name 属性的名字<br>
             * value 属性的值<br>
             *//*
            @Override
            public boolean apply(Object source, String name, Object value) {
            
                if (source instanceof Area && name.equals("subareas")) {
                    return true;
                }
                return false;
            }
        });*/
        
        //调用父类的方法转化成json字符串并写回数据
        page2json(page, jsonConfig);
        
        return NONE;
        
    }
    
    //*****************************用于定区关联分区*****************************************
    
    //查询所有未关联定区的分区
    @Action(value="subareaAction_findSubAreaUnAssociated")
    public String findSubAreaUnAssociated() throws Exception{
        
        //调用service层的方法查询为关联定区的分区
        List<SubArea> list = subareaService.findSubAreaUnAssociated();
        
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[]{"subareas","couriers"});
        
        //调用父类的方法转化成json字符串并写回数据
        list2json(list, jsonConfig);
        
        return NONE;
    }
    
    //根据定区id去查询已经关联的分区
    @Action(value="subareaAction_findSubAreaAssociated2FixedArea")
    public String findSubAreaAssociated2FixedArea() throws Exception{
        
        //调用service层的方法根据定区去查询已经关联的分区
        List<SubArea> list = subareaService.findSubAreaAssociated2FixedArea(getModel().getFixedArea());
        
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[]{"subareas","couriers"});
        
        //调用父类的方法转化成json字符串并写回数据
        list2json(list, jsonConfig);
        
        return NONE;
        
    }
    
    //使用属性驱动接收所有要关联对应定区的分区ID数组
    private Long[] subAreaIds;
    
    public void setSubAreaIds(Long[] subAreaIds) {
        this.subAreaIds = subAreaIds;
    }
    
    //保存关联分区信息
    @Action(value="subareaAction_assignSubAreas2FixedArea",results={
            @Result(name="success",location="/pages/base/fixed_area.html",type="redirect")})
    public String subareaAction_assignSubAreas2FixedArea(){
        
        // 先把关联到定区ID的客户全部解绑
        // 再去进行绑定
        subareaService.associationCourierToFixedArea(getModel().getFixedArea(),subAreaIds);
        
        return SUCCESS;
        
    }
    
  //*****************************用于定区关联分区*****************************************

}
  
