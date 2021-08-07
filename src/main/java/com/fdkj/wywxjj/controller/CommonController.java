package com.fdkj.wywxjj.controller;

import com.fdkj.wywxjj.base.CusResponseBody;
import com.fdkj.wywxjj.config.BusConfig;
import com.fdkj.wywxjj.config.ServerConfig;
import com.fdkj.wywxjj.error.BusinessException;
import com.fdkj.wywxjj.utils.file.FileUploadUtils;
import com.fdkj.wywxjj.utils.file.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;

/**
 * 通用业务处理
 *
 * @author wyt
 */
@Controller
@RequestMapping("common")
public class CommonController {

    private static final Logger log = LoggerFactory.getLogger(CommonController.class);

    @Autowired
    private ServerConfig serverConfig;

    /**
     * 通用下载请求
     *
     * @param fileName 文件名称
     * @param delete   是否删除
     */
    @RequestMapping("download")
    public void downloadFile(HttpServletRequest request, HttpServletResponse response,
                             @RequestParam("fileName") String fileName, @RequestParam("delete")Boolean delete) {
        try {
            if (!FileUtils.checkAllowDownload(fileName)) {
                throw new Exception("文件名称" + fileName + "非法，不允许下载。 ");
            }
            String realFileName = System.currentTimeMillis() + fileName.substring(fileName.indexOf("_") + 1);
            String filePath = BusConfig.getDownLoadBaseDir() + File.separator + fileName;
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            FileUtils.setAttachmentResponseHeader(response, realFileName);
            FileUtils.writeBytes(filePath, response.getOutputStream());
            if (delete) {
                FileUtils.deleteFile(filePath);
            }
        } catch (Exception e) {
            log.error("下载文件失败", e);
            throw new BusinessException("下载文件失败", HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
        }
    }

    /**
     * 通用上传请求
     */
    @RequestMapping("upload")
    public ResponseEntity<CusResponseBody> uploadFile(MultipartFile file) {
        try {
            // 上传文件路径
            String filePath = BusConfig.getUploadBaseDir();
            // 上传并返回新文件名称
            String fileName = FileUploadUtils.upload(filePath, file);
            String url = serverConfig.getUrl() + fileName;
            //构造返回数据
            CusResponseBody cusResponseBody = CusResponseBody.success("上传成功");
            cusResponseBody.put("fileName", fileName);
            cusResponseBody.put("url", url);
            return new ResponseEntity<>(cusResponseBody, HttpStatus.OK);
        } catch (Exception e) {
            log.error("上传失败", e);
            throw new BusinessException("上传失败: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
        }
    }
}
