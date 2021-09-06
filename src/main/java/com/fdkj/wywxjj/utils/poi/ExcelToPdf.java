package com.fdkj.wywxjj.utils.poi;

import com.aspose.cells.License;
import com.aspose.cells.PdfSaveOptions;
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
            InputStream is = ExcelToPdf.class.getClassLoader().getResourceAsStream("receipt/license.xml");
            License license = new License();
            license.setLicense(is);
        } catch (Exception e) {
            log.error("获取license 失败 -> {}", e.getLocalizedMessage());
        }
    }

    public static void excel2pdf(InputStream inputStream, OutputStream outputStream) throws Exception {
        //去水印
        //getLicense();

        Workbook wb = new Workbook(inputStream);

        PdfSaveOptions pdfSaveOptions = new PdfSaveOptions();
        //把内容放在一张PDF 页面上；
        pdfSaveOptions.setOnePagePerSheet(true);

        //wb.save(outputStream, SaveFormat.PDF);
        wb.save(outputStream, pdfSaveOptions);
        outputStream.flush();
        outputStream.close();
        inputStream.close();
    }

}

