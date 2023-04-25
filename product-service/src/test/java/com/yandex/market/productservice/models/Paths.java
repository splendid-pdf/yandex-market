package com.yandex.market.productservice.models;

import static com.yandex.market.util.HttpUtils.PUBLIC_API_V1;

public class Paths {
    public static final String SELLER_PATH = "/" + PUBLIC_API_V1 + "/sellers/{sellerId}/",
            SELLER_PRODUCT_PATH = SELLER_PATH + "products",
            PRODUCT_PATH = SELLER_PATH + "products/{productId}",
            SELLER_ARCHIVED_PATH = SELLER_PATH + "archive/products",
            CHANGE_PRICE_PATH = SELLER_PATH + "products/{productId}/price",
            CHANGE_COUNT_PATH = SELLER_PATH + "products/{productId}/count",
            ADD_IMAGE_PATH = SELLER_PATH + "products/{productId}/images",
            CHANGE_VISIBILITY_PATH = SELLER_PATH + "products/visibility",
            PATH_TO_MINIMAL_PRODUCT = "src/test/resources/json/create_product_with_minimal.json",
            PATH_TO_MAX_PRODUCT = "src/test/resources/json/create_product_with_max_fields.json",
            PATH_TO_UPDATE_PRODUCT = "src/test/resources/json/update_product_name_and_desc.json";
}
