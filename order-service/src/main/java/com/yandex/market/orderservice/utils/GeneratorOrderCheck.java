package com.yandex.market.orderservice.utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.yandex.market.orderservice.model.Order;
import com.yandex.market.orderservice.model.OrderedProduct;
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
    private final static int TAX = 20;
    private static BaseFont baseFont = null;

    static {
        try {
            baseFont = BaseFont.createFont("order-service/src/main/resources/fonts/arial.ttf",
                    BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        } catch (DocumentException | IOException e) {
            log.error("Error occurred while getting the font for receipt generation: %s"
                    .formatted(e.getMessage()));
        }
    }

    private final static Font fontSmallText = new Font(baseFont, FONT_SIZE_SMALLER, Font.NORMAL);
    private final static Font fontBigText = new Font(baseFont, FONT_SIZE_BIGGER, Font.BOLD);
    private final static DecimalFormat doubleFormat = new DecimalFormat("#0.00");
    private final static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm");
    private final static String BORD = "-     -     -     -     -     -     -     -     -    -    -" +
            "   -   -   -   -   -   -  -  -  -  -  -  -  -  - - -\n";
    private final static Phrase border = new Phrase(BORD, fontBigText);
    private final static int LEFT = Element.ALIGN_LEFT;
    private final static int CENTER = Element.ALIGN_CENTER;
    private final static int RIGHT = Element.ALIGN_RIGHT;

    public static ByteArrayInputStream generate(Order order) throws IOException, DocumentException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()){
            Document check = new Document();
            PdfWriter.getInstance(check, outputStream);
            check.open();
            check.add(createTitleParagraph(order));
            check.add(border);
            check.add(createTableProducts(order));
            check.add(border);
            check.add(createTableTotal(order));
            check.add(border);
            check.close();
            return new ByteArrayInputStream(outputStream.toByteArray());
        } catch (DocumentException | IOException e) {
            log.error("Generation of check of order don't completed: %s".formatted(e.getMessage()));
            throw e;
        }
    }

    private static void initCell(PdfPTable table, Font font, String text, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setHorizontalAlignment(alignment);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
    }

    private static PdfPTable initTable(final float[] relativeWidths) {
        PdfPTable table = new PdfPTable(relativeWidths);
        table.setHorizontalAlignment(LEFT);
        table.setWidthPercentage(100);
        table.setSpacingBefore(0f);
        table.setSpacingAfter(0f);
        return table;
    }

    private static Paragraph createTitleParagraph(Order order) {
        Paragraph title = new Paragraph();
        title.setFont(fontSmallText);
        title.setAlignment(CENTER);
        title.add(
                "Кассовый чек № " + order.getId() + "\n" +
                        dateFormat.format(order.getPaymentDateTime()) + "\n" +
                        "Marketplace\n" +
                        "ИНН 12345678" + "\n" +
                        "Вид налогообложения: ОСН\n" +
                        "Приход\n"
        );
        return title;
    }

    private static PdfPTable createTableProducts(Order order) {
        PdfPTable products = initTable(new float[]{10, 65, 25});
        for (int i = 0; i < order.getOrderedProducts().size(); i++) {
            double price = order.getOrderedProducts().get(i).getPrice();
            double amount = order.getOrderedProducts().get(i).getAmount();
            String name = order.getOrderedProducts().get(i).getName();

            initCell(products, fontSmallText, i + 1 + ".", LEFT);
            initCell(products, fontSmallText, name, LEFT);
            initCell(products, fontSmallText, "", LEFT);
            initCell(products, fontSmallText, "", LEFT);
            initCell(products, fontSmallText, amount + " x " + price, LEFT);
            initCell(products, fontSmallText, "=" + doubleFormat.format(price * amount), RIGHT);
            initCell(products, fontSmallText, "", LEFT);
            initCell(products, fontSmallText, "в т.ч. НДС " + TAX + "%", LEFT);
            initCell(products, fontSmallText,
                    "=" + doubleFormat.format(price - ((price * 100) / (100 + TAX))), RIGHT);

            PdfPCell emptyRow = new PdfPCell(new Phrase(" "));
            emptyRow.setColspan(3);
            emptyRow.setBorder(Rectangle.NO_BORDER);
            products.addCell(emptyRow);
        }
        return products;
    }

    private static PdfPTable createTableTotal(Order order) {
        PdfPTable total = initTable(new float[]{50, 50});
        initCell(total, fontBigText, "ИТОГ", LEFT);
        initCell(total, fontBigText, "=" + doubleFormat.format(order.getPrice()), RIGHT);
        initCell(total, fontSmallText, "в т.ч. НДС " + TAX + "%", LEFT);
        initCell(total, fontSmallText, "=" + doubleFormat.format(sumTaxProducts(order)), RIGHT);
        initCell(total, fontSmallText, "Полный расчет ", LEFT);
        initCell(total, fontSmallText, "=" + doubleFormat.format(order.getPrice()), RIGHT);
        return total;
    }

    private static Double sumTaxProducts(Order order) {
        return order.getOrderedProducts().stream()
                .mapToDouble(OrderedProduct::getPrice)
                .map(price -> price - ((price * 100) / (100 + TAX)))
                .sum();
    }
}