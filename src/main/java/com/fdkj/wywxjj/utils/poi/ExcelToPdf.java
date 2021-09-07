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

    public static boolean getLicense() {
        boolean result = false;
        try {
            InputStream is = com.aspose.cells.License.class.getResourceAsStream("/com.aspose.cells.lic_2999.xml");
            License asposeLicense = new License();
            asposeLicense.setLicense(is);
            is.close();
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void excel2pdf(InputStream inputStream, OutputStream outputStream) throws Exception {
        //去水印
        getLicense();

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

