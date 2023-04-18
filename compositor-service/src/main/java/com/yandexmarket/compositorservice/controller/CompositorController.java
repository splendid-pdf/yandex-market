package com.yandexmarket.compositorservice.controller;

import com.yandexmarket.compositorservice.dto.ProductPreviewMainPage;
import com.yandexmarket.compositorservice.service.CompositorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.yandex.market.util.HttpUtils.PUBLIC_API_V1;

@RestController
@RequiredArgsConstructor
@RequestMapping(PUBLIC_API_V1)
public class CompositorController {

    private final CompositorService compositorService;

    @GetMapping("/compositors/newest-product")
    public List<ProductPreviewMainPage> getProductPreviewImage(){
        return compositorService.getNewestProducts();
    }
}
