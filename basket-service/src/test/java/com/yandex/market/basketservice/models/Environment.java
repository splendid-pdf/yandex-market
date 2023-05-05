package com.yandex.market.basketservice.models;

import static com.yandex.market.util.HttpUtils.PUBLIC_API_V1;

public class Environment {

    public static final String
            BASKET_PATH = "/" + PUBLIC_API_V1 + "/users/{userId}/basket/products";


    public static final String USER_ID_1 = "6a2e63a7-a8b7-4a5e-9422-6a16ee963e8d";
    public static final String USER_ID_2 = "ef3d5941-2ead-442c-ab45-113d464591d5";

    public static final String PATH_BASKET_RESPONSE = "src/test/resources/json/BasketResponse.json";
}
