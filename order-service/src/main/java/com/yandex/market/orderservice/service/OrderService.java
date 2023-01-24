package com.yandex.market.orderservice.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.yandex.market.orderservice.dto.OrderPreviewDto;
import com.yandex.market.orderservice.dto.OrderRequestDto;
import com.yandex.market.orderservice.dto.OrderResponseDto;
import com.yandex.market.orderservice.mapper.OrderMapper;
import com.yandex.market.orderservice.model.Order;
import com.yandex.market.orderservice.model.OrderStatus;
import com.yandex.market.orderservice.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final OrderMapper orderMapper;

    public OrderResponseDto create(OrderRequestDto orderRequestDto, UUID userId) {
        Order order = orderMapper.toOrder(orderRequestDto);
        order.setExternalId(UUID.randomUUID());
        order.setOrderStatus(OrderStatus.CREATED);
        order.setUserId(userId);
        order.setCreationTimestamp(LocalDateTime.now());
        return orderMapper.toOrderResponseDto(orderRepository.save(order));
    }

    public OrderResponseDto getByExternalId(UUID externalId) {
        if (!orderRepository.existsOrderByExternalId(externalId)) {
            log.error("this order is not found");
            throw new EntityNotFoundException("this order is not found");
        }
        Order order = orderRepository.getOrderByExternalId(externalId);
        return orderMapper.toOrderResponseDto(order);
    }

    public Page<OrderPreviewDto> getOrdersByUserId(UUID userId, Pageable pageable) {
        Page<Order> pagedResult = orderRepository.getOrderByUserId(userId, pageable);
        return new PageImpl<>(pagedResult.getContent()
                .stream()
                .map(orderMapper::toOrderPreviewDto)
                .collect(Collectors.toList()));
    }

    public String cancelOrder(UUID externalId) {
        Order order = orderRepository.getOrderByExternalId(externalId);
        if (order.getOrderStatus().equals(OrderStatus.COMPLETED)) {
            log.error("order is completed");
            throw new RuntimeException();
        }
        order.setOrderStatus(OrderStatus.CANCELED);
        orderRepository.save(order);
        return "order is cancel";
    }

    public OrderResponseDto update(OrderRequestDto orderRequestDto, UUID externalId) {
        Order orderByExternalId = orderRepository.getOrderByExternalId(externalId);
        Order order = orderMapper.toOrder(orderRequestDto);
        order.setId(orderByExternalId.getId());
        if (orderByExternalId.getOrderStatus().equals(OrderStatus.COMPLETED)) {
            log.error("order is completed");
            throw new RuntimeException();
        }
        return orderMapper.toOrderResponseDto(orderRepository.save(order));
    }

    public ByteArrayInputStream createCheck(UUID externalId) throws FileNotFoundException, DocumentException {
        Order order = orderRepository.getOrderByExternalId(externalId);
        Document document = new Document();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        //PdfWriter.getInstance(document,new FileOutputStream("c:/Users/AlekseiZhi/Desktop/itext.pdf"));
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
            table.addCell(Double.toString(order.getOrderedProducts().get(i).getPrice() * order.getOrderedProducts().get(i).getAmount()));
        }

        document.add(new Paragraph("do"));
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));
        document.add(new Paragraph("table"));
        document.add(table);
        document.add(new Paragraph("posle"));
        document.close();
        return new ByteArrayInputStream(outputStream.toByteArray());
    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }
}