package com.example.testocr.enums;

import lombok.*;

/**
 * @Author SN
 * @Date 2024/4/4 19:44
 * @Description: BUG 退散
 */

@AllArgsConstructor
@NoArgsConstructor
@ToString
public enum Pathfiles {

    ORIGIN_IMG("D:\\allinstalltoolexe\\jTessBoxEditorFX\\mytestimg\\temp\\","上传的图片的临时保存位置"),
    DEAL_IMG("D:\\allinstalltoolexe\\jTessBoxEditorFX\\mytestimg\\temp\\","经过opencv处理后的图片保存位置");

    private String path;
    private String desc;
    public String getPath() {
        return path;
    }
}
