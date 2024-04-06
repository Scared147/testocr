package com.example.testocr.config;

import com.example.testocr.enums.Pathfiles;
import net.sourceforge.tess4j.Tesseract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @作者: 
 * @日期: 2023/10/12 22:58
 * @描述:
 */
@Configuration
public class TesseractOcrConfiguration {

   @Autowired
   private PathStatic pathStatic;

   @Bean
   public Tesseract tesseract() {

      Tesseract tesseract = new Tesseract();
      // 设置训练数据文件夹路径
      tesseract.setDatapath(pathStatic.getStaticFilePath(""));  //获取chi_sim.traineddata所在的文件夹绝对路径
      // 设置为中文简体
      tesseract.setLanguage("chi_sim");
      return tesseract;
   }
}