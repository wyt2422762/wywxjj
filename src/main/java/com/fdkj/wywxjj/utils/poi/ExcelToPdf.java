package com.fdkj.wywxjj.utils.poi;

import com.aspose.cells.License;
import com.aspose.cells.SaveFormat;
import com.aspose.cells.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * excel 转 pdf
 *
 * @author wyt
 */
public class ExcelToPdf {
    private static final Logger log = LoggerFactory.getLogger(ExcelToPdf.class);

    private static void getLicense() {
        try {
//            File templateFile = FileUtil.file("classpath:templates/license.xml");
//            FileInputStream inputStream = new FileInputStream(templateFile);
//            ClassPathResource classPathResource = new ClassPathResource("classpath:templates/license.xml");
            InputStream is = ExcelToPdf.class.getClassLoader().getResourceAsStream("templates/license.xml"); //  license.xml应放在..\WebRoot\WEB-INF\classes路径下
            License aposeLic = new License();
            aposeLic.setLicense(is);
        } catch (Exception e) {
            log.error("获取license 失败 -> {}", e.getLocalizedMessage());
        }
    }

    public static void excel2pdf(InputStream inputStream, OutputStream outputStream) throws Exception {
        //getLicense();
        Workbook wb = new Workbook(inputStream);// 原始excel路径
        wb.save(outputStream, SaveFormat.PDF);
    }

}

