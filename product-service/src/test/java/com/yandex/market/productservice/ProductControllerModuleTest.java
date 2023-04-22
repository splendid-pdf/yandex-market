package com.yandex.market.productservice;

import com.yandex.market.productservice.dto.projections.SellerProductPreview;
import com.yandex.market.productservice.service.ProductSellerServiceTest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;

import static com.yandex.market.util.HttpUtils.PUBLIC_API_V1;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@ActiveProfiles("testcontainers")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@TestPropertySource(locations = "classpath:application-testcontainers.yml")
@Sql(
        scripts = "classpath:db/insert_tests_products.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
class ProductControllerModuleTest {

    private final ProductSellerServiceTest serviceTest;

    private final UUID REAL_SELLER_ID = UUID.fromString("cb041d31-a345-4d80-971a-70c49cbc5c28");

    private final UUID REAL_PRODUCT_ID = UUID.fromString("0a751cc5-4325-444e-ac1d-df48c678d7b1");

    private final String SELLER_PATH = "/" + PUBLIC_API_V1 + "/sellers/{sellerId}/";
    private final String SELLER_PRODUCT_PATH = SELLER_PATH + "products";
    private final String SELLER_ARCHIVED_PATH = SELLER_PATH + "archive/products";
    private final String CHANGE_PRICE_PATH = SELLER_PATH + "products/{productId}/price";
    private final String CHANGE_COUNT_PATH = SELLER_PATH + "products/{productId}/count";
    private final String CHANGE_ARCHIVE_PATH = SELLER_PATH + "products/archive";
    private final String CHANGE_VISIBILITY_PATH = SELLER_PATH + "products/visibility";


    @Test
    void test () {
        assertTrue(true);
    }

    @Test
    @Transactional
    void shouldFindPageProductsBySellerIdSuccessfulSearch() throws Exception {
        long expectedTotalElements = 2;
        Page<SellerProductPreview> products = serviceTest.getProducts(SELLER_PRODUCT_PATH, REAL_SELLER_ID);
        Assertions.assertNotNull(products);
        assertEquals(expectedTotalElements, products.getTotalElements());
    }

    @Test
    @Transactional
    void shouldFindArchivedProductsPageBySellerIdSuccessfulSearch() throws Exception {
        long expectedTotalElements = 1;
            Page<SellerProductPreview> products = serviceTest.getArchivedProducts(SELLER_ARCHIVED_PATH, REAL_SELLER_ID);
        Assertions.assertNotNull(products);
        assertEquals(expectedTotalElements, products.getTotalElements());
    }

    @Test
    @Transactional
    void shouldChangePrice_Successful() throws Exception {
        long updatePrice = 1234L;

//        serviceTest.changePrice(CHANGE_PRICE_PATH, REAL_SELLER_ID, REAL_PRODUCT_ID, updatePrice);
        serviceTest.changePriceForProduct(CHANGE_PRICE_PATH, REAL_SELLER_ID, REAL_PRODUCT_ID, updatePrice);

        Long actualPrice = serviceTest.findProductByExternalId(REAL_PRODUCT_ID).price();
        assertEquals(updatePrice, actualPrice, "Received price not as expected");
    }
}

