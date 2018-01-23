package cn.zzz.bos.fore.web.action;

import java.util.concurrent.TimeUnit;

import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;

import com.aliyuncs.exceptions.ClientException;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import cn.zzz.bos.utils.SmsUtils;
import cn.zzz.crm.domain.Customer;
import cn.zzz.utils.MailUtils;

/**  
 * ClassName:CustomerAction <br/>  
 * Function:  <br/>  
 * Date:     Jan 21, 2018 4:32:11 PM <br/>       
 */
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("struts-default")
public class CustomerAction extends ActionSupport implements ModelDriven<Customer>{
    
    private Customer model = new Customer();

    @Override
    public Customer getModel() {
          
        return model;
        
    }
    
    //**********************************发送验证码*******************************************
    
    @Action(value="customerAction_sendSMS")
    public String sendSMS(){
        
        try {
            
            //获取随机验证码
            String code = RandomStringUtils.randomNumeric(6);
            System.out.println("code =" + code);
            
            //将验证码存入session域，方便注册的时候校验
            ServletActionContext.getRequest().getSession().setAttribute("serverCode", code);
            
            //调用工具类发送短信
            //SmsUtils.sendSms(model.getTelephone(), code);
            
        } catch (Exception e) {
              
            e.printStackTrace();  
            
        }
        
        return NONE;
        
    }
    //**********************************发送验证码*******************************************
    
    //************************************注册**********************************************
    
    //使用属性驱动获取页面填写的验证码
    private String checkcode;
    public void setCheckcode(String checkcode) {
        this.checkcode = checkcode;
    }
    
    //注册
    /*@Action(value="customerAction_regist",results={
            @Result(name="success",location="/signup-success.html",type="redirect"),
            @Result(name="error",location="/signup-fail.html",type="redirect")})
    public String regist(){
        
        //比较页面上传的验证码和服务器的验证码是否相同，相同才给予注册
        String serverCode = (String) ServletActionContext.getRequest().getSession().getAttribute("serverCode");
        if(StringUtils.isNotEmpty(checkcode) && StringUtils.isNotEmpty(serverCode) && serverCode.equals(checkcode)){
            
            //调用crm服务器的保存
            WebClient
                    .create("http://localhost:8180/crm/webService/customerService/save")
                    .type(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .post(model);
            
            return SUCCESS;
            
        }
        
        return ERROR;
        
    }*/
    
    //注入Redis模板
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    
    //注册
    @Action(value="customerAction_regist",results={
            @Result(name="success",location="/signup-success.html",type="redirect"),
            @Result(name="error",location="/signup-fail.html",type="redirect")})
    public String regist(){
        
        //比较页面上传的验证码和服务器的验证码是否相同，相同才给予注册
        String serverCode = (String) ServletActionContext.getRequest().getSession().getAttribute("serverCode");
        if(StringUtils.isNotEmpty(checkcode) && StringUtils.isNotEmpty(serverCode) && serverCode.equals(checkcode)){
            
            //调用crm服务器的保存
            WebClient
                    .create("http://localhost:8180/crm/webService/customerService/save")
                    .type(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .post(model);
            
            //生成邮件激活验证码
            /**
             *  //产生5位长度的随机字符串，中文环境下是乱码
                RandomStringUtils.random(5);

                //使用指定的字符生成5位长度的随机字符串
                RandomStringUtils.random(5, new char[]{'a','b','c','d','e','f', '1', '2', '3'});
                
                //生成指定长度的字母和数字的随机组合字符串
                RandomStringUtils.randomAlphanumeric(5);
                
                //生成随机数字字符串
                RandomStringUtils.randomNumeric(5);
                
                //生成随机[a-z]字符串，包含大小写
                RandomStringUtils.randomAlphabetic(5);
                
                //生成从ASCII 32到126组成的随机字符串 
                RandomStringUtils.randomAscii(4)
             */
            String activeCode = RandomStringUtils.randomAlphanumeric(36);
            
            System.out.println(activeCode);
            
            //将验证码保存到Redis
            redisTemplate.opsForValue().set(getModel().getTelephone(), activeCode, 1, TimeUnit.DAYS);
            // 拼接邮件内容
            String emailBody = "感谢您注册速运快递 ,请您在24小时内点击下面的链接激活您的帐号<br><a href='http://localhost:8280/portal/customerAction_active.action?telephone="
                    + getModel().getTelephone() + "&activeCode=" + activeCode + "'>激活帐号</a>" + "或者复制下面链接激活" + "http://localhost:8280/portal/customerAction_active.action?telephone=" + getModel().getTelephone() + "&activeCode=" + activeCode;
            //发送激活邮件
            MailUtils.sendMail(getModel().getEmail(), "激活邮件", emailBody);
            
            return SUCCESS;
            
        }
        
        return ERROR;
        
    }
    
    //************************************注册**********************************************
    
    //************************************激活**********************************************
    
    //使用属性驱动获取用户邮件链接中的激活码
    private String activeCode;
    public void setActiveCode(String activeCode) {
        this.activeCode = activeCode;
    }
    
    //激活用户
    @Action(value="customerAction_active",results={
            @Result(name="success",location="/active_success.html",type="redirect"),
            @Result(name="error",location="/active_error.html",type="redirect"),
            @Result(name="actived",location="/active_actived.html",type="redirect")})
    public String active(){
        
        //获取存储在Redis中的激活码
        String serverActiveCode = redisTemplate.opsForValue().get(getModel().getTelephone());
        //比对两个激活码
        if(StringUtils.isNotEmpty(serverActiveCode) && StringUtils.isNotEmpty(activeCode) && serverActiveCode.equals(activeCode)){
            
            //查看用户是否已经激活
            Customer customer = WebClient
                    .create("http://localhost:8180/crm/webService/customerService/findByTelephone")
                    .type(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .query("telephone", getModel().getTelephone())
                    .get(Customer.class);
            
            if(customer.getType() != null && customer.getType() == 1){
                return "actived";
            }
            
            //激活用户
            WebClient
                    .create("http://localhost:8180/crm/webService/customerService/active")
                    .type(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .query("telephone", getModel().getTelephone())
                    .put(null);
            
            //删除Redis中的数据
            redisTemplate.delete(getModel().getTelephone());
            
            return SUCCESS;
            
        }
        
        return ERROR;
        
    }
    
    //************************************激活**********************************************
    
    //************************************登录**********************************************
    
    @Action(value="customerAction_login",results={
            @Result(name="success",location="/index.html",type="redirect"),
            @Result(name="login",location="login.html",type="redirect")})
    public String login(){
        
        //获取用户名和密码
        String telephone = getModel().getTelephone();
        String password = getModel().getPassword();
        
        //获取服务器上的登录验证码
        String serverLoginCode = (String) ServletActionContext.getRequest().getSession().getAttribute("validateCode");
        
        //比对验证码
        if(StringUtils.isNotEmpty(serverLoginCode) && StringUtils.isNotEmpty(checkcode) && serverLoginCode.equals(checkcode)){
            
            //请求crm查询看是否有这个客户存在
            Customer customer = WebClient
                    .create("http://localhost:8180/crm/webService/customerService/login")
                    .type(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .query("telephone", telephone)
                    .query("password", password)
                    .get(Customer.class);
            
            if(customer != null){
                
                //登录成功,将用户存储到域对象
                ServletActionContext.getRequest().getSession().setAttribute("customer", customer);
                return SUCCESS;
                
            }else {
                //登录失败
                return LOGIN;
            }
            
        }
        //验证码不对
        return LOGIN;
        
    }
    
    //************************************登录**********************************************

}
  
