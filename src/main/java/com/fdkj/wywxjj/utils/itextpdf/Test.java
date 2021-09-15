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

import java.io.*;
import java.util.List;

public class Test {

    public static final float topMargin = 114f;
    public static final float bottomMargin = 156f;
    public static final float leftMargin = 90f;
    public static final float rightMargin = 90f;

    private static final String DEST = "C:\\Users\\Administrator\\Downloads\\091510244786\\test.pdf";

    private static final String HTML = "C:\\Users\\Administrator\\Downloads\\091510244786\\方案结算明细打印.html";

    private static final String FONT = "C:\\Users\\Administrator\\Downloads\\091510244786\\simhei.ttf";

    private static final String FONTS = "C:\\Users\\Administrator\\Downloads\\091510244786\\fonts";

    public static void main(String[] args) throws Exception {

        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(DEST));

        ConverterProperties props = new ConverterProperties();

        FontProvider fp = new FontProvider();
        fp.addStandardPdfFonts();
        fp.addDirectory(FONTS);
        props.setFontProvider(fp);

        List<IElement> iElements = HtmlConverter.convertToElements(new FileInputStream(HTML), props);

        Document document = new Document(pdfDocument, PageSize.A4, true); // immediateFlush设置true和false都可以，false 可以使用 relayout

        document.setMargins(0, 10, 0, 10);

        for (IElement iElement : iElements) {
            BlockElement blockElement = (BlockElement) iElement;
            blockElement.setMargins(0, 10f, 0, 10f);
            document.add(blockElement);
        }
        document.flush();
        document.close();
    }

}
