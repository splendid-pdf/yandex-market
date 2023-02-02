package com.yandex.market.orderservice.utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.yandex.market.orderservice.model.Order;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@Slf4j
public class GeneratorOfCheck {

    public static ByteArrayInputStream generate(Order order) {
        Document document = new Document();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, outputStream);

            document.open();
            Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA, 20, BaseColor.BLACK);
            document.add(new Paragraph("YANDEX MARKET =)", titleFont));
            document.add(new Paragraph(" "));
            document.add(new Paragraph("141004, Russia, Moscow, Mitishi, st.Selikatnaya 19", titleFont));
            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));
            PdfPTable table = new PdfPTable(6);
            PdfPCell c1 = new PdfPCell(new Phrase("Number"));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);

            c1 = new PdfPCell(new Phrase("Product"));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);

            c1 = new PdfPCell(new Phrase("Product price"));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);

            c1 = new PdfPCell(new Phrase("Amount"));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);

            c1 = new PdfPCell(new Phrase("VAT"));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);

            c1 = new PdfPCell(new Phrase("sum shopping"));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);

            for (int i = 0; i < order.getOrderedProducts().size(); i++) {
                table.addCell(Integer.toString(i));
                table.addCell(order.getOrderedProducts().get(i).getName());
                table.addCell(Double.toString(order.getOrderedProducts().get(i).getPrice()));
                table.addCell(Double.toString(order.getOrderedProducts().get(i).getAmount()));
                table.addCell(Double.toString(20) + "%");
                table.addCell(Double.toString(order.getOrderedProducts().get(i).getPrice()
                        * order.getOrderedProducts().get(i).getAmount()));
            }

            document.add(new Paragraph("do"));
            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));
            document.add(new Paragraph("table"));
            document.add(table);
            document.add(new Paragraph("posle"));
            document.close();
        } catch (DocumentException e) {
            log.error("Operation dont completed: %s".formatted(e.getMessage()));
        }

        return new ByteArrayInputStream(outputStream.toByteArray());
    }
}
