package com.example.testocr.config;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class PathStatic {

    private final ResourceLoader resourceLoader;

    public PathStatic(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    //获取static下文件的绝对路径
    public String getStaticFilePath(String fileName) {
        Resource resource = resourceLoader.getResource("classpath:/static/" + fileName);
        try {
            return resource.getFile().getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
