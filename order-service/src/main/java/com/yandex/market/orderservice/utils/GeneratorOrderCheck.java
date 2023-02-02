package com.yandex.market.orderservice.utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.yandex.market.orderservice.model.Order;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;

@Slf4j
public class GeneratorOrderCheck {
    private final static int FONT_SIZE_SMALLER = 10;
    private final static int FONT_SIZE_BIGGER = 16;
    private final static DecimalFormat doubleFormat = new DecimalFormat("#0.00");
    private final static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm");
    private final static String BORD = "-     -     -     -     -     -     -     -     -    -    -" +
            "   -   -   -   -   -   -  -  -  -  -  -  -  -  - - -\n";

    public static ByteArrayInputStream generate(Order order) {
        Document check = new Document();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        double taxSum = 0;

        try {
            PdfWriter.getInstance(check, outputStream);

            BaseFont baseFont = BaseFont.createFont("c:/Windows/Fonts/arial.ttf",
                    BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font fontSmallText = new Font(baseFont, FONT_SIZE_SMALLER, Font.NORMAL, BaseColor.BLACK);
            Font fontBigText = new Font(baseFont, FONT_SIZE_BIGGER, Font.BOLD, BaseColor.BLACK);

            Paragraph title = new Paragraph();
            title.setFont(fontSmallText);
            title.setAlignment(Element.ALIGN_CENTER);

            title.add("Кассовый чек № " + order.getId() + "\n");
            title.add(dateFormat.format(order.getPaymentDateTime())+ "\n");
            title.add("Marketplace");
            title.add("ИНН 12345678" + "\n"); //TODO привязать название поставщика, ИНН
            title.add("Вид налогообложения: ОСН\n");
            title.add("Приход\n");

            Phrase border = new Phrase(BORD, fontBigText);

            PdfPTable products = initTable(new float[]{10, 65, 25});
            for (int i = 0; i < order.getOrderedProducts().size(); i++) {
                double price = order.getOrderedProducts().get(i).getPrice();
                double amount = order.getOrderedProducts().get(i).getAmount();
                String name = order.getOrderedProducts().get(i).getName();

                initCell(products, fontSmallText, i + 1 + ".", Element.ALIGN_LEFT);
                initCell(products, fontSmallText, name, Element.ALIGN_LEFT);
                initCell(products, fontSmallText, "", Element.ALIGN_LEFT);
                initCell(products, fontSmallText, "", Element.ALIGN_LEFT);
                initCell(products, fontSmallText, amount + " x " + price, Element.ALIGN_LEFT);
                initCell(products, fontSmallText, "=" + doubleFormat.format(price * amount),
                        Element.ALIGN_RIGHT);
                initCell(products, fontSmallText, "", Element.ALIGN_LEFT);
                initCell(products, fontSmallText, "в т.ч. НДС 20%", Element.ALIGN_LEFT);
                initCell(products, fontSmallText,
                        "=" + doubleFormat.format(price - (price * 100 / 120)), Element.ALIGN_RIGHT);

                PdfPCell emptyRow = new PdfPCell(new Phrase(" "));
                emptyRow.setColspan(3);
                emptyRow.setBorder(Rectangle.NO_BORDER);
                products.addCell(emptyRow);

                taxSum += price - (price * 100 / 120);
            }

            PdfPTable total = initTable(new float[]{50, 50});
            initCell(total, fontBigText, "ИТОГ", Element.ALIGN_LEFT);
            initCell(total, fontBigText,"=" + doubleFormat.format(order.getPrice()), Element.ALIGN_RIGHT);
            initCell(total, fontSmallText, "в т.ч. НДС 20% ", Element.ALIGN_LEFT);
            initCell(total, fontSmallText, "=" + doubleFormat.format(taxSum), Element.ALIGN_RIGHT);
            initCell(total, fontSmallText, "Полный расчет ", Element.ALIGN_LEFT);
            initCell(total, fontSmallText, "=" + doubleFormat.format(order.getPrice()), Element.ALIGN_RIGHT);

            check.open();
            check.add(title);
            check.add(border);
            check.add(products);
            check.add(border);
            check.add(total);
            check.add(border);
            check.close();
        } catch (DocumentException | IOException e) {
            log.error("Generation of check of order don't completed: %s".formatted(e.getMessage()));
        }
        return new ByteArrayInputStream(outputStream.toByteArray());
    }

    private static void initCell(PdfPTable table,
                                 Font font,
                                 String text,
                                 int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setHorizontalAlignment(alignment);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
    }

    private static PdfPTable initTable(final float[] relativeWidths) {
        PdfPTable table = new PdfPTable(relativeWidths);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.setWidthPercentage(100);
        table.setSpacingBefore(0f);
        table.setSpacingAfter(0f);
        return table;
    }
}