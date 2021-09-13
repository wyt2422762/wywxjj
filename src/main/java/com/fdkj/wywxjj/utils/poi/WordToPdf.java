package com.fdkj.wywxjj.utils.poi;


import com.aspose.words.Document;
import com.aspose.words.License;
import com.aspose.words.SaveFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * word
 * @author wyt
 */
public class WordToPdf {
    private static final Logger log = LoggerFactory.getLogger(WordToPdf.class);

    public static boolean getLicense() {
        boolean result = false;
        try {
//            InputStream is = WordToPdf.class.getClassLoader().getResourceAsStream("receipt/wordLicense.xml");
            InputStream is = com.aspose.words.Document.class
                    .getResourceAsStream("/com.aspose.words.lic_2999.xml");
            License asposeLicense = new License();
            asposeLicense.setLicense(is);
            is.close();
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void word2pdf(InputStream inputStream, OutputStream outputStream) throws Exception {
        //去水印
        getLicense();

        Document doc = new Document(inputStream);
        doc.save(outputStream, SaveFormat.PDF);

        outputStream.flush();
        outputStream.close();
        inputStream.close();
    }
}
