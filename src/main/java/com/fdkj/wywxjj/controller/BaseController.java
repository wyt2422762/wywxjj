package com.fdkj.wywxjj.controller;


import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.hutool.core.io.FileUtil;
import com.fdkj.wywxjj.utils.poi.ExcelToPdf;
import org.apache.poi.ss.usermodel.Workbook;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URLEncoder;
import java.util.Map;

/**
 * 基础controller
 *
 * @author wyt
 */
public class BaseController {

    /**
     * 单据pdf
     *
     * @param response     resp
     * @param templatePath 单据模板路劲
     * @param params       参数
     * @param showName     pdf文件名称 xxx.pdf
     * @throws Exception err
     */
    public void downLoadReceipt(HttpServletResponse response, String templatePath, Map<String, Object> params, String showName) throws Exception {
        TemplateExportParams templateExportParams = new TemplateExportParams(templatePath);

        Workbook workbook = ExcelExportUtil.exportExcel(templateExportParams, params);
        response.setCharacterEncoding("UTF-8");
        response.addHeader("Access-Control-Allow-Headers", "Content-Disposition");
        response.addHeader("Access-Control-Expose-Headers", "Content-Disposition");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(showName, "UTF-8"));
        File tempFile = FileUtil.touch("classpath:/receipt/temp/" + System.currentTimeMillis() + ".xlsx");
        FileOutputStream outputStream = new FileOutputStream(tempFile);
        workbook.write(outputStream);
        FileInputStream inputStream = new FileInputStream(tempFile);
        ExcelToPdf.excel2pdf(inputStream, response.getOutputStream());
        tempFile.delete();
    }

}
