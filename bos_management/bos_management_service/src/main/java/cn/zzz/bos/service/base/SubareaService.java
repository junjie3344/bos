package cn.zzz.bos.service.base;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import cn.zzz.bos.domain.base.FixedArea;
import cn.zzz.bos.domain.base.SubArea;

/**  
 * ClassName:SubareaService <br/>  
 * Function:  <br/>  
 * Date:     Jan 18, 2018 5:22:02 PM <br/>       
 */
public interface SubareaService {

    //保存分区管理
    void save(SubArea subArea);

    //分页查询
    Page<SubArea> findAll(Pageable pageable);

    //条件分页查询
    Page<SubArea> findAll(Specification<SubArea> specification, Pageable pageable);

    //*****************************用于定区关联分区*****************************************
    //查询所有未关联定区的分区
    List<SubArea> findSubAreaUnAssociated();

    //根据定区id去查询已经关联的分区
    List<SubArea> findSubAreaAssociated2FixedArea(FixedArea fixedArea);

    //保存关联分区信息
    void associationCourierToFixedArea(FixedArea fixedArea, Long[] subAreaIds);
    //*****************************用于定区关联分区*****************************************
    
}
  
