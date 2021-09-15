package com.fdkj.wywxjj.utils.itextpdf;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.BlockElement;
import com.itextpdf.layout.element.IElement;
import com.itextpdf.layout.font.FontProvider;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.List;

/**
 * html 转 pdf
 *
 * @author wyt
 */
public class Html2Pdf {

    public static void html2Pdf(InputStream inputStream, OutputStream outputStream) throws IOException {
        ClassLoader classLoader = Html2Pdf.class.getClassLoader();
        URL url = classLoader.getResource("");
        String fontsPath = url.getPath() + "fonts";

        ConverterProperties props = new ConverterProperties();

        FontProvider fp = new FontProvider();
        fp.addStandardPdfFonts();
        fp.addDirectory(fontsPath);
        props.setFontProvider(fp);

        List<IElement> iElements = HtmlConverter.convertToElements(inputStream, props);

        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(outputStream));

        Document document = new Document(pdfDocument, PageSize.A4, true);

        for (IElement iElement : iElements) {
            BlockElement blockElement = (BlockElement) iElement;
            blockElement.setMargins(0, 10f, 0, 10f);
            document.add(blockElement);
        }
        document.flush();
        document.close();
    }

}
