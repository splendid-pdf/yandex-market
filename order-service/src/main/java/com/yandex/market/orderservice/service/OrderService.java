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
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private static final String COMPLETED_ORDER_CAN_NOT_BE_UPDATED = "Completed order can not be updated";
    private static final String ORDER_BY_EXTERNAL_ID_IS_NOT_FOUND_MESSAGE = "Order not found by external id = '%s'";

    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;

    @Transactional
    public UUID create(OrderRequestDto orderRequestDto, UUID userId) {
        Order order = orderMapper.toOrder(orderRequestDto);
        order.setUserId(userId);
        orderRepository.save(order);
        return order.getExternalId();
    }

    public OrderResponseDto getOrderResponseDtoByExternalId(UUID externalId) {
        return orderMapper.toOrderResponseDto(getOrderByExternalId(externalId));
    }

    @Transactional(readOnly = true)
    public Page<OrderPreviewDto> getOrdersByUserId(UUID userId, Pageable pageable) {
        Page<Order> pagedResult = orderRepository.getOrderByUserId(userId, pageable);
        return new PageImpl<>(pagedResult.getContent()
                .stream()
                .map(orderMapper::toOrderPreviewDto)
                .collect(Collectors.toList()));
    }

    @Transactional
    public void cancelOrder(UUID externalId) {
        Order order = getOrderByExternalId(externalId);

        checkIfOrderIsCompletedAndThrowUOE(order);

        order.setOrderStatus(OrderStatus.CANCELED);
    }

    @Transactional
    public OrderResponseDto update(OrderRequestDto orderRequestDto, UUID externalId) {
        Order storedOrder = getOrderByExternalId(externalId);

        checkIfOrderIsCompletedAndThrowUOE(storedOrder);

        Order order = orderMapper.toOrder(orderRequestDto);
        order.setId(storedOrder.getId());

        return orderMapper.toOrderResponseDto(orderRepository.save(order));
    }

    @SneakyThrows
    public ByteArrayInputStream createCheck(UUID externalId) {
        Order order = getOrderByExternalId(externalId);
        Document document = new Document();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
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

    private Order getOrderByExternalId(UUID externalId) {
        return orderRepository.getOrderByExternalId(externalId)
                .orElseThrow(() -> new EntityNotFoundException(
                        ORDER_BY_EXTERNAL_ID_IS_NOT_FOUND_MESSAGE.formatted(externalId)));
    }

    private static void checkIfOrderIsCompletedAndThrowUOE(Order order) {
        if (OrderStatus.COMPLETED == order.getOrderStatus()) {
            throw new UnsupportedOperationException(COMPLETED_ORDER_CAN_NOT_BE_UPDATED);
        }
    }
}