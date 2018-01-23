package cn.zzz.bos.dao.base.impl;

import java.lang.reflect.Array;

import cn.zzz.bos.utils.PinYin4jUtils;

/**  
 * ClassName:PinYinTest <br/>  
 * Function:  <br/>  
 * Date:     Jan 17, 2018 4:27:35 PM <br/>       
 */
public class PinYinTest {
    
    public static void main(String[] args) {
        
        //简码GDSZBA      城市编码SHENZHEN
        
        String provice = "广东省";
        String city = "深圳市";
        String district = "宝安区";
        
        //先把后面的省市区去掉
        provice = provice.substring(0, provice.length() - 1);
        city = city.substring(0, city.length() - 1);
        district = district.substring(0, district.length() - 1);
        
        //调用PinYin4jUtils工具类生成城市编码
        //参数1 ： 要转换成汉字的字符串
        //参数2 ： 中间以什么来隔开
        //toUpperCase : 转换成大写
        String citycode = PinYin4jUtils.hanziToPinyin(city, "").toUpperCase();
        System.out.println(citycode);
        
        //调用PinYin4jUtils工具类生成简码
        String[] headByString = PinYin4jUtils.getHeadByString(provice+city+district);
        //headByString : [G,D,S,Z,B,A]
        //然后在调用其工具类把其转换成
        String shortcode = PinYin4jUtils.stringArrayToString(headByString);
        System.out.println(shortcode);
        //然后headByString就
        
    }

}
  
