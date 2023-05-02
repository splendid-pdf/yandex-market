package com.yandex.market.productservice.models;

import java.util.List;
import java.util.UUID;

import static com.yandex.market.util.HttpUtils.PUBLIC_API_V1;

public class Environment {
    public static final String
            PRODUCTS_PATH = "/" + PUBLIC_API_V1 + "/sellers/{sellerId}/products",
            TYPES_PATH = "/" + PUBLIC_API_V1 + "/types",
            ARCHIVE_PATH = "/" + PUBLIC_API_V1 + "/sellers/{sellerId}/archive/products",
            PRODUCT_PATH = PRODUCTS_PATH + "/{productId}",
            CHANGE_PRICE_PATH = PRODUCTS_PATH + "/{productId}/price",
            CHANGE_COUNT_PATH = PRODUCTS_PATH + "/{productId}/count",
            CHANGE_VIS_PATH = PRODUCTS_PATH + "/visibility",
            IMAGE_PATH = PRODUCTS_PATH + "/{productId}/images",
            DEL_IMAGE_PATH = PRODUCTS_PATH + "/images",
            SPECIAL_PRICE_PATH = PRODUCTS_PATH + "/{productId}/special-prices",
            DEFINITE_SP_PATH = PRODUCTS_PATH + "/special-prices/{specialPriceId}",
            CHARACTER_PATH = PRODUCTS_PATH + "/characteristics/{characteristicId}",
            PRODUCT_PREVIEW_PATH = "/" + PUBLIC_API_V1 + "/product-previews",
            MIN_PRODUCT_PATH = "src/test/resources/json/create_product_with_minimal.json",
            MAX_PRODUCT_PATH = "src/test/resources/json/create_product_with_max_fields.json",
            UPD_PRODUCT_PATH = "src/test/resources/json/update_product_name_and_desc.json",
            CREATE_SP_PATH = "src/test/resources/json/special-price/create-special-price.json",
            CREATE_CHAR__PATH = "src/test/resources/json/characteristic/create-characteristics.json";

    public static final String AUTH_TOKEN = "Bearer eyJraWQiOiJjNWVmNTRlYy05NTExLTQyNzAtOGZhNC1hYWFlYjM4MDU5OTQiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJzZWxsZXJfZm9yX3Rlc3RzQGdtYWlsLmNvbSIsImF1ZCI6ImNsaWVudCIsIm5iZiI6MTY4MzAxNzI0Miwic2NvcGUiOlsib3BlbmlkIl0sImlzcyI6Imh0dHA6Ly81MS4yNTAuMTAyLjEyOjkwMDAiLCJleHAiOjE2ODM2MjIwNDIsImlhdCI6MTY4MzAxNzI0Miwic2VsbGVyLWlkIjoiY2IwNDFkMzEtYTM0NS00ZDgwLTk3MWEtNzBjNDljYmM1YzI4IiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9TRUxMRVIiXX0.cl_AGBdeQ7dQCwSAKFjjUA7eGFTkecdAwzg0stCaxUVzjXXS0wVqRkKUHR_CaWSdkGn1xMTrk0l-d70a9ReUF2LPXao0GWauWSup7EYiV3epFvqslRrHWf1pNLUutQdmCgc79yzd1OAR2i_YJ9wZ9A2B8hOZL9WY1_Hu6799NlGkO3u2PJc0R--minV9fHaV5kLgVoInleXAH06ulDyHNO5DHyhcyeMzPSzC9IcC0o4nRRm7_Blux1sjdQa57u4HxpQjkj8o7NGiPh3k5ibTxzdbRUhUrcUisCinVeOF--FUn7fBieswi0A358U8M4-yMNBRUG63Um5RWOafckRu4Q";

    public static final UUID REAL_SELLER_ID = UUID.fromString("cb041d31-a345-4d80-971a-70c49cbc5c28");
    public static final UUID REAL_TYPE_ID = UUID.fromString("301c5370-be41-421e-9b15-f1e80a7079f9");
    public static final UUID REAL_SP_ID = UUID.fromString("ee6d7da1-32ac-4d24-84ec-adc5c0ca9552");
    public static final UUID REAL_CHAR_ID = UUID.fromString("f3bb4ee9-c624-472b-8e4d-669dc863267d");
    public static final UUID UNREAL_SELLER_ID = UUID.fromString("cb041d31-a345-4d80-971a-70c49cbc5c99");
    public static final UUID REAL_PRODUCT_ID = UUID.fromString("0a751cc5-4325-444e-ac1d-df48c678d7b1");

    public static final List<UUID> REAL_PRODUCTS = List.of(
            REAL_PRODUCT_ID,
            UUID.fromString("0a751cc5-4325-444e-ac1d-df48c678d7b2"),
            UUID.fromString("0a751cc5-4325-444e-ac1d-df48c678d7b3"),
            UUID.fromString("0a751cc5-4325-444e-ac1d-df48c678d7b4")
    );

    public static final long ARCHIVED = 1;
}
