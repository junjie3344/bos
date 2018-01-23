package cn.zzz.bos.dao.base.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

/**  
 * ClassName:POITest <br/>  
 * Function:  <br/>  
 * Date:     Jan 17, 2018 4:18:34 PM <br/>       
 */
public class POITest {

    public static void main(String[] args) throws Exception {

        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(new FileInputStream("C:\\Users\\princess\\Desktop\\xxx.xls"));
        
        //读取工作簿
        HSSFSheet sheetAt = hssfWorkbook.getSheetAt(0);
        
        //遍历行
        for (Row row : sheetAt) {
            //获取行号
            int rowNum = row.getRowNum();
            //如果是第一行，就结束当次循环，因为我们不需要第一行的数据
            if(rowNum == 0){
                continue;
            }
            
            //遍历列
            for (Cell cell : row) {
                String value = cell.getStringCellValue();
                System.out.print(value + "\t");
            }
            
            System.out.println();
            
        }
        
        hssfWorkbook.close();
        
    }

}
  
