package com.yandex.market.productservice.models;

import static com.yandex.market.util.HttpUtils.PUBLIC_API_V1;

public class Paths {
    public static final String
            PRODUCTS_PATH = "/" + PUBLIC_API_V1 + "/sellers/{sellerId}/products",
            ARCHIVE_PATH = "/" + PUBLIC_API_V1 + "/sellers/{sellerId}/archive/products",
            PRODUCT_PATH = PRODUCTS_PATH + "/{productId}",
            CHANGE_PRICE_PATH = PRODUCTS_PATH + "/{productId}/price",
            CHANGE_COUNT_PATH = PRODUCTS_PATH + "/{productId}/count",
            ADD_IMAGE_PATH = PRODUCTS_PATH + "/{productId}/images",
            CHANGE_VIS_PATH = PRODUCTS_PATH + "/visibility",
            MIN_PRODUCT_PATH = "src/test/resources/json/create_product_with_minimal.json",
            MAX_PRODUCT_PATH = "src/test/resources/json/create_product_with_max_fields.json",
            UPD_PRODUCT_PATH = "src/test/resources/json/update_product_name_and_desc.json";
}
