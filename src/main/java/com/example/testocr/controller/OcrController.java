package com.example.testocr.controller;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RandomUtil;
import com.example.testocr.config.PathStatic;
import com.example.testocr.enums.Pathfiles;
import com.example.testocr.service.OcrService;
import lombok.AllArgsConstructor;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RequestMapping("/api")
@RestController
@AllArgsConstructor
public class OcrController {

    @Autowired
    private OcrService ocrService;

    @Autowired
    private PathStatic pathStatic;

    private final Tesseract tesseract;


    //不经过处理的ocr识别
    @PostMapping(value = "/recognize", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String recognizeImage(@RequestParam("file") MultipartFile file) throws TesseractException, IOException {
      // 调用OcrService中的方法进行文字识别
        String s = ocrService.recognizeText(file);
        System.out.println(s);
        return s;
    }

    //经过opencv处理的ocr识别
    @PostMapping("/recognize2")
    public String recognizeIdCard(@RequestParam("file") MultipartFile file) throws TesseractException, IOException {
        System.load(pathStatic.getStaticFilePath("opencv_java460.dll"));
        Path tempFile = Files.createDirectories(Paths.get(Pathfiles.ORIGIN_IMG.getPath() + RandomUtil.randomNumbers(3) + ".png"));
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);
        }
        // 读取图像
        Mat originmat = Imgcodecs.imread((tempFile.getParent() + "\\" + tempFile.getFileName()));

        // 图像去噪
        Mat denoisedImage = new Mat();
        Imgproc.GaussianBlur(originmat, denoisedImage, new Size(3, 3), 0);

        // 图像增强（增加对比度和亮度）
        Mat enhancedImage = new Mat();
        Core.addWeighted(denoisedImage, 1.5, denoisedImage, -0.5, 0, enhancedImage);

        // 图像预处理（例如：灰度化、二值化）
        Mat grayImage = new Mat();
        Imgproc.cvtColor(enhancedImage, grayImage, Imgproc.COLOR_BGR2GRAY);
        Mat threshImage = new Mat();
        Imgproc.threshold(grayImage, threshImage, 0, 255, Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);

        // 4. 找到身份证的轮廓
        Mat hierarchy = new Mat();
        List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(threshImage, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        // 5. 选择最大轮廓
        double maxArea = -1;
        MatOfPoint maxContour = null;
        for (MatOfPoint contour : contours) {
            double area = Imgproc.contourArea(contour);
            if (area > maxArea) {
                maxArea = area;
                maxContour = contour;
            }
        }
        // 6. 创建掩码
        Mat mask = new Mat(threshImage.size(), CvType.CV_8UC1, Scalar.all(0));
        Imgproc.drawContours(mask, contours, contours.indexOf(maxContour), new Scalar(255), -1);
        // 7. 应用掩码
        Mat result = new Mat();
        threshImage.copyTo(result, mask);
        // 8. 剪裁身份证
        Rect boundingRect = Imgproc.boundingRect(maxContour);
        Mat croppedImage = new Mat(result, boundingRect);

        MatOfByte byteMat = new MatOfByte();
        Imgcodecs.imencode(".png", croppedImage, byteMat);
        byte[] byteArray = byteMat.toArray();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
        BufferedImage bufferedImage = ImageIO.read(inputStream);
        File outputFile = new File(Pathfiles.DEAL_IMG.getPath()+"result" + RandomUtil.randomString(3) + ".png");
        ImageIO.write(bufferedImage, "png", outputFile);
        String text = tesseract.doOCR(bufferedImage);
        System.out.println(text); //打印结果
        FileUtil.del(tempFile);//删除临时文件
        return text;
    }



}