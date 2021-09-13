package com.fdkj.wywxjj.controller;


import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.hutool.core.io.FileUtil;
import com.aspose.cells.PdfSaveOptions;
import com.fdkj.wywxjj.config.BusConfig;
import com.fdkj.wywxjj.utils.poi.ExcelToPdf;
import com.fdkj.wywxjj.utils.poi.WordToPdf;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
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
    public void downLoadReceipt(HttpServletResponse response, String templatePath, Map<String, Object> params, String showName, PdfSaveOptions pdfSaveOptions) throws Exception {
        TemplateExportParams templateExportParams = new TemplateExportParams(templatePath);

        Workbook workbook = ExcelExportUtil.exportExcel(templateExportParams, params);
        response.setCharacterEncoding("UTF-8");
        response.addHeader("Access-Control-Allow-Headers", "Content-Disposition");
        response.addHeader("Access-Control-Expose-Headers", "Content-Disposition");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(showName, "UTF-8"));
        File tempFile = FileUtil.touch(BusConfig.getTempBaseDir() + File.separator + System.currentTimeMillis() + ".xlsx");
        FileOutputStream outputStream = new FileOutputStream(tempFile);
        workbook.write(outputStream);
        outputStream.flush();
        outputStream.close();
        FileInputStream inputStream = new FileInputStream(tempFile);
        ExcelToPdf.excel2pdf(inputStream, response.getOutputStream(), pdfSaveOptions);
        tempFile.delete();
    }


    public void freemarkerWord(HttpServletResponse response, String templateName, Map<String, Object> params, String showName) throws Exception {
        response.setCharacterEncoding("UTF-8");
        response.addHeader("Access-Control-Allow-Headers", "Content-Disposition");
        response.addHeader("Access-Control-Expose-Headers", "Content-Disposition");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(showName, "UTF-8"));

        //创建配置实例
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_31);

        //设置编码
        cfg.setDefaultEncoding("UTF-8");

        //设置模板加载文件夹
        ClassLoader classLoader = getClass().getClassLoader();
        URL url = classLoader.getResource("");
        String path = url.getPath() + File.separator + "freemarker";

        cfg.setDirectoryForTemplateLoading(new File(path));
        cfg.setDefaultEncoding("UTF-8");

        //获取模板
        Template template = cfg.getTemplate(templateName);

        File tempFile = FileUtil.touch(BusConfig.getTempBaseDir() + File.separator + System.currentTimeMillis() + ".docx");
        FileOutputStream outputStream = new FileOutputStream(tempFile);
        Writer out = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

        template.process(params, out);
        outputStream.close();
        out.close();

        FileInputStream inputStream = new FileInputStream(tempFile);
        WordToPdf.word2pdf(inputStream, response.getOutputStream());

        tempFile.delete();
    }

    /**
     * 生成统计区间
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return res
     */
    public String buildStaticSection(String startDate, String endDate) {
        if (StringUtils.isBlank(startDate) && StringUtils.isBlank(endDate)) {
            return null;
        }
        return (StringUtils.isBlank(startDate) ? "" : startDate.trim()) + " 至 " + (StringUtils.isBlank(endDate) ? "" : endDate.trim());
    }
}
