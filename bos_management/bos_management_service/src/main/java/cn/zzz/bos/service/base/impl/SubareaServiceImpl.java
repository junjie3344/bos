package cn.zzz.bos.service.base.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.zzz.bos.dao.base.SubareaRepository;
import cn.zzz.bos.domain.base.FixedArea;
import cn.zzz.bos.domain.base.SubArea;
import cn.zzz.bos.service.base.SubareaService;

/**  
 * ClassName:SubareaServiceImpl <br/>  
 * Function:  <br/>  
 * Date:     Jan 18, 2018 5:22:17 PM <br/>       
 */
@Service
@Transactional
public class SubareaServiceImpl implements SubareaService {
    
    @Autowired
    private SubareaRepository subareaRepository;

    //保存的方法
    @Override
    public void save(SubArea subArea) {
          
        subareaRepository.save(subArea);
        
    }

    //分页查询
    @Override
    public Page<SubArea> findAll(Pageable pageable) {
          
        //直接调用dao层的方法查询所有
        return subareaRepository.findAll(pageable);
        
    }

    //条件分页查询
    @Override
    public Page<SubArea> findAll(Specification<SubArea> specification, Pageable pageable) {
          
        return subareaRepository.findAll(specification, pageable);
        
    }

    //*****************************用于定区关联分区*****************************************
    //查询所有未关联定区的分区
    @Override
    public List<SubArea> findSubAreaUnAssociated() {
          
        //调用dao层的方法查询所有未关联定区的分区
        return subareaRepository.findByFixedAreaIsNull();
        
    }

    //根据定区id去查询已经关联的分区
    @Override
    public List<SubArea> findSubAreaAssociated2FixedArea(FixedArea fixedArea) {
          
        //调用dao层的方法去查询所有已经关联定区的分区
        return subareaRepository.findByFixedArea(fixedArea);
        
    }

    //保存关联分区信息
    @Override
    public void associationCourierToFixedArea(FixedArea fixedArea, Long[] subAreaIds) {
          
        if(fixedArea != null && fixedArea.getId() != null){
            // 先把关联到定区的客户全部解绑
            subareaRepository.unbindByFixedAreaId(fixedArea);
            if(subAreaIds != null && subAreaIds.length > 0){
                for (Long id : subAreaIds) {
                    // 再去进行绑定
                    subareaRepository.bindFixedAreaById(fixedArea,id);
                }
            }
        }
        
    }
    //*****************************用于定区关联分区*****************************************
    
}
  
