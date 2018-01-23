package cn.zzz.crm.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.zzz.crm.dao.CustomerRepository;
import cn.zzz.crm.domain.Customer;
import cn.zzz.crm.service.CustomerService;

/**  
 * ClassName:CustomerServiceImpl <br/>  
 * Function:  <br/>  
 * Date:     Jan 20, 2018 3:14:56 PM <br/>       
 */
@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {
    
    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public List<Customer> findAll() {
          
        return customerRepository.findAll();
        
    }

    // 查找未关联定区的客户
    @Override
    public List<Customer> findCustomersUnAssociated() {
          
        return customerRepository.findByFixedAreaIdIsNull();
        
    }

    // 查找关联到指定定区的客户
    @Override
    public List<Customer> findCustomersAssociated2FixedArea(String fixedAreaId) {
          
        return customerRepository.findByFixedAreaId(fixedAreaId);
        
    }

    @Override
    public void assignCustomers2FixedArea(String fixedAreaId, Long[] customerIds) {
        
        if(StringUtils.isNotEmpty(fixedAreaId)){
            
            // 先把关联到定区ID的客户全部解绑
            //调用dao层的方法根据定区id把所有客户解绑
            customerRepository.unbindByFixedAreaId(fixedAreaId);
            
            if(customerIds != null && customerIds.length > 0){
                for (Long id : customerIds) {
                    
                    // 再次进行绑定
                    //调用dao层的方法根据定区id将取与客户id相关的数据绑定
                    customerRepository.bindFixedAreaById(fixedAreaId,id);
                    
                }
            }
            
        }
        
    }

    //保存客户
    @Override
    public void save(Customer customer) {
          
        //调用dao层的方法保存客户
        customerRepository.save(customer);
        
    }

    //根据手机号查找用户
    @Override
    public Customer findByTelephone(String telephone) {
          
        //直接调用dao层的方法根据手机号查找客户
        return customerRepository.findByTelephone(telephone);
        
    }

    //激活用户
    @Override
    public void active(String telephone) {
          
        //调用dao层的方法根据手机号激活用户
        customerRepository.active(telephone);
        
    }

    //登录
    @Override
    public Customer login(String telephone, String password) {
          
        //根据手机号和密码查找用户
        return customerRepository.findByTelephoneAndPassword(telephone,password);
        
    }

    // 根据地址查询定区ID
    @Override
    public String findFixedAreaIdByAddress(String address) {
          
        return customerRepository.findFixedAreaIdByAddress(address);
        
    }

}
  
