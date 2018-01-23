package cn.zzz.crm.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.zzz.crm.domain.Customer;

/**  
 * ClassName:CustomerRepository <br/>  
 * Function:  <br/>  
 * Date:     Jan 20, 2018 3:15:21 PM <br/>       
 */
public interface CustomerRepository extends JpaRepository<Customer, Long>{

    // 查找未关联定区的客户
    List<Customer> findByFixedAreaIdIsNull();

    // 查找关联到指定定区的客户
    List<Customer> findByFixedAreaId(String fixedAreaId);

    //根据定区id把以其相关的客户信息全部解绑
    @Modifying
    @Query("update Customer set fixedAreaId = null where fixedAreaId = ?1")
    void unbindByFixedAreaId(String fixedAreaId);

    //根据id将其数据与定区id绑定
    @Modifying
    @Query("update Customer set fixedAreaId = ?1 where id = ?2")
    void bindFixedAreaById(String fixedAreaId, Long id);

    //根据手机号查找客户
    Customer findByTelephone(String telephone);

    //根据手机号激活用户
    @Modifying
    @Query("update Customer set type = 1 where telephone = ?1")
    void active(String telephone);

    //根据用户名和密码查找用户
    Customer findByTelephoneAndPassword(String telephone, String password);
    
    //根据地址查询定区ID
    @Query("select fixedAreaId from Customer where address = ?1")
    String findFixedAreaIdByAddress(String address);

}
  
