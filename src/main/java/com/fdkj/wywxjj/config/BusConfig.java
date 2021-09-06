package com.fdkj.wywxjj.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * 业务配置
 * @author wyt
 */
@Component
public class BusConfig {

    /**
     * 文件上传下载基本路径
     */
    private static String fileBaseDir;

    @Value("${bus.fileBaseDir}")
    public void setFileBaseDir(String fileBaseDir) {
        BusConfig.fileBaseDir = fileBaseDir;
    }

    /**
     * 获取上传文件基础路径
     *
     * @return 基础路径
     */
    public static String getUploadBaseDir() {
        return fileBaseDir + File.separator + "upload";
    }

    /**
     * 获取文件下载基础路径
     *
     * @return 基础路径
     */
    public static String getDownLoadBaseDir() {
        return fileBaseDir + File.separator + "download";
    }

    /**
     * 获取临时文件路径
     */
    public static String getTempBaseDir() {
        return fileBaseDir + File.separator + "temp";
    }
}
