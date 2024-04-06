package com.example.testocr;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class TestocrApplication {
    private static final Logger Log = LoggerFactory.getLogger(TestocrApplication.class);

    public static void main(String[] args) {

        SpringApplication app = new SpringApplication(TestocrApplication.class);
        Environment env = app.run(args).getEnvironment();
        Log.info("启动成功！！");
        Log.info("地址：\thttp://localhost:{}{}/test",env.getProperty("server.port"),env.getProperty("server.servlet.context-path"));

    }

}
