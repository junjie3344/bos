package cn.zzz.bos.dao.base;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.zzz.bos.domain.base.Area;

/**  
 * ClassName:AreaRepository <br/>  
 * Function:  <br/>  
 * Date:     Jan 17, 2018 5:30:43 PM <br/>       
 */
public interface AreaRepository extends JpaRepository<Area, Long>{

    //根据输入框输入的值模糊查询
    @Modifying
    @Query("from Area where province like ?1 or city like ?1 or district like ?1 or postcode like ?1 or citycode like ?1 or shortcode like ?1")
    List<Area> findQ(String q);
    
    //根据省市区查找地区
    Area findByProvinceAndCityAndDistrict(String province,String city,String district);

}
  
