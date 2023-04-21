//package com.yandex.market.productservice;
//
//import com.yandex.market.productservice.service.ProductSellerServiceTest;
//import lombok.RequiredArgsConstructor;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.TestPropertySource;
//import org.springframework.test.context.jdbc.Sql;
//import org.springframework.transaction.annotation.Transactional;
//import org.testcontainers.junit.jupiter.Testcontainers;
//
//import java.util.List;
//import java.util.UUID;
//
//import static com.yandex.market.util.HttpUtils.PUBLIC_API_V1;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//@SpringBootTest
//@Testcontainers
//@AutoConfigureMockMvc
//@RequiredArgsConstructor(onConstructor = @__(@Autowired))
//@ActiveProfiles("testcontainers")
//@Sql(scripts = {
//        "classpath:db/insert_tests_products.sql",
//        "classpath:db/insert_types.sql"
//},
//        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
//@TestPropertySource(locations = "classpath:application-testcontainers.yml")
//public class ProductControllerTest {
//    private final ProductSellerServiceTest serviceTest;
//
//    private final UUID REAL_SELLER_ID = UUID.fromString("cc859910-6b60-42f5-ab68-26974edd9552");
//
//    private final List<UUID> REAL_PRODUCTS = List.of(
//            UUID.fromString("62bce524-29c3-4698-b0d2-7bb76b21e701"),
//            UUID.fromString("62bce524-29c3-4698-b0d2-7bb76b21e702"),
//            UUID.fromString("62bce524-29c3-4698-b0d2-7bb76b21e703")
//    );
//
//    private final UUID REAL_PRODUCT_ID = UUID.fromString("62bce524-29c3-4698-b0d2-7bb76b21e723");
//
//    private final String SELLER_PATH = "/" + PUBLIC_API_V1 + "/sellers/",
//            CHANGE_PRICE_PATH = SELLER_PATH + "/{sellerId}/products/{productId}/price",
//            CHANGE_COUNT_PATH = SELLER_PATH + "/{sellerId}/products/{productId}/count",
//            DELETE_PRODUCT_PATH = SELLER_PATH + "/{sellerId}/products",
//            CHANGE_ARCHIVE_PATH = SELLER_PATH + "/{sellerId}/products/archive",
//            CHANGE_VISIBILITY_PATH = SELLER_PATH + "/{sellerId}/products/visibility";
//
//    @Test
//    @Transactional
//    void shouldChangeProductPriceSellerAndProductFoundAndChanged() throws Exception {
//        long updatePrice = 30L;
//
//        serviceTest.changePrice(
//                REAL_SELLER_ID,
//                REAL_PRODUCT_ID,
//                updatePrice,
//                CHANGE_PRICE_PATH
//        );
//
//        Long actualPrice = serviceTest.findProductByExternalId(REAL_PRODUCT_ID).price();
//        assertEquals(updatePrice, actualPrice, "Received price not as expected");
//    }
//}
//
//
