package com.example.testocr.controller;


import com.example.testocr.service.OcrService;
import lombok.AllArgsConstructor;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Controller
@AllArgsConstructor
public class TestController {

    @Autowired
    private OcrService ocrService;

    @GetMapping(value = "/test")
    public String test() {
      return "index";
    }
}