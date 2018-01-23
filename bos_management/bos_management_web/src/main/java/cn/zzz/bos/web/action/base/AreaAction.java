package cn.zzz.bos.web.action.base;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
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

import cn.zzz.bos.domain.base.Area;
import cn.zzz.bos.service.base.AreaService;
import cn.zzz.bos.utils.PinYin4jUtils;
import cn.zzz.bos.web.action.CommonAction;
import net.sf.json.JsonConfig;

/**  
 * ClassName:AreaAction <br/>  
 * Function:  <br/>  
 * Date:     Jan 17, 2018 5:28:53 PM <br/>       
 */
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("struts-default")
public class AreaAction extends CommonAction<Area> {
    
    public AreaAction() {
          
        super(Area.class);  
        
    }

    @Autowired
    private AreaService areaService;
    
    //使用属性驱动，获取用户上传的文件
    private File file;
    
    //使用属性驱动，获取用户上传的文件的文件名
    private String fileFileName;
    
    @Action(value="areaAction_importXLS",results={
            @Result(name="error",location="/pages/error.html",type="redirect"),
            @Result(name="success",location="/pages/base/area.html",type="redirect")})
    public String importXLS(){
        
        try {
            
            // 加载文件
            HSSFWorkbook hssfWorkbook = new HSSFWorkbook(new FileInputStream(file));
            
            // 读取第1个工作簿的内容
            HSSFSheet sheetAt = hssfWorkbook.getSheetAt(0);
            
            // 用来装Area的集合
            List<Area> list = new ArrayList<>();
            
            // 遍历行
            for (Row row : sheetAt) {
                
                //第一行是标题，忽略第一行的内容
                if(row.getRowNum() == 0){
                    continue;
                }
                
                // 获取数据
                //获取省份
                String province = row.getCell(1).getStringCellValue();
                //获取城市
                String city = row.getCell(2).getStringCellValue();
                //获取区域
                String district = row.getCell(3).getStringCellValue();
                //获取邮政编码
                String postcode = row.getCell(4).getStringCellValue();
                
                //切割掉后面的省市区
                province = province.substring(0, province.length() - 1);
                city = city.substring(0, city.length() - 1);
                district = district.substring(0, district.length() - 1);
                
                //获得城市编码
                String citycode = PinYin4jUtils.hanziToPinyin(city, "").toUpperCase();
                
                //获得简码
                String[] headByString = PinYin4jUtils.getHeadByString(province + city + district);
                String shortcode = PinYin4jUtils.stringArrayToString(headByString);
                
                //构造一个Area对象
                Area area = new Area();
                area.setProvince(province);
                area.setCity(city);
                area.setDistrict(district);
                area.setPostcode(postcode);
                area.setCitycode(citycode);
                area.setShortcode(shortcode);
                
                // 添加到集合
                list.add(area);
                
            }
            //调用service的方法保存数据
            areaService.save(list);
            
            // 释放资源
            hssfWorkbook.close();
            
        } catch (Exception e) {
              
            e.printStackTrace();
            
            return ERROR;
            
        }
        
        return SUCCESS;
        
    }
    
    //分页查询
    @Action(value="areaAction_pageQuery")
    public String pageQuery() throws Exception{
        
        Pageable pageable = new PageRequest(page - 1, rows);
        
        //调用service层的方法分页查询
        Page<Area> page = areaService.findAll(pageable);
        
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[]{"subareas"});
        
        //调用父类的方法写回json
        page2json(page, jsonConfig);
        
        return NONE;
        
    }
    //使用属性驱动获取用户在输入框中输入的内容,用于模糊查询
    private String q;
    
    //查询所有，用于分区管理的增加按钮的下拉框
    @Action(value="areaAction_findAll")
    public String findAll() throws Exception{
        
        List<Area> list;
        
        if(StringUtils.isNotEmpty(q)){
            //如果输入框有值，就让其去模糊查询
            list = areaService.findQ(q);
        }else {
            
            //如果输入框为空就查询所有
            
            //调用service层的方法进行查询，把null传过去相当于调用其dao层的findAll()方法，它会查询该类对应表中的所有数据
            Page<Area> page = areaService.findAll(null);
            
            //获取所有数据
            list = page.getContent();
            
        }
        
        //设置要忽略的属性
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[] {"subareas"});
        
        //调用父类的方法处理并写回数据
        list2json(list, jsonConfig);
        
        return NONE;
        
    }

    public void setFile(File file) {
        this.file = file;
    }
    
    public void setFileFileName(String fileFileName) {
        this.fileFileName = fileFileName;
    }
    
    public void setQ(String q) {
        this.q = q;
    }
    
}
  
