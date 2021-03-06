package com.wz.front.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;

/**
 * @Author: Cherry
 * @Date: 2021/1/10
 * @Desc: FileUtils
 */
@Component
public class FileUtils {

    @Autowired
    private ServletContext servletContext;

    public String getFileUploadPath() {
        // 如果没有写文件上传路径,保存到临时目录
        String realPath = System.getProperty("java.io.tmpdir");
//        String realPath = servletContext.getRealPath(File.separator);
        if (isWinOs()) {
            return realPath + "\\icbmsUpload\\";
        } else {
            return realPath + "/icbmsUpload/";
        }
    }

    public Boolean isWinOs() {
        String os = System.getProperty("os.name");
        if (os.toLowerCase().startsWith("win")) {
            return true;
        } else {
            return false;
        }
    }
}
