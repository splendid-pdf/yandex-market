package com.yandex.market.productservice;

import com.yandex.market.productservice.service.ProductSellerServiceTest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yaml")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@SqlGroup({
        @Sql(value = "classpath:db/insert_types.sql"),
        @Sql(value = "classpath:db/insert_tests_fields.sql")
})
public class SellerControllerTest {
    private final ProductSellerServiceTest serviceTest;
    @Value("${spring.app.seller.url}")
    private String PATH_TO_SELLER;

    private final UUID REAL_SELLER_ID = UUID.fromString("81fe9426-2891-4f13-a3b7-6e238fb731b3");

    private final UUID REAL_PRODUCT_ID = UUID.fromString("62bce524-29c3-4698-b0d2-7bb76b21e723");

    private final UUID UNREAL_SELLER_ID = UUID.fromString("00678201-f3c8-4d5c-a628-2344eef50c61");

    private final UUID UNREAL_PRODUCT_ID = UUID.fromString("00678201-f3c8-4d5c-a628-2344eef50c62");

    private final List<UUID> REAL_PRODUCT_IDS = new ArrayList<>(List.of(
            UUID.fromString("62bce524-29c3-4698-b0d2-7bb76b21e724"),
            UUID.fromString("62bce524-29c3-4698-b0d2-7bb76b21e725")
    ));

    @Value("${spring.app.seller.url}" + "{sellerId}/products/{productId}/price")
    private String CHANGE_PRICE_PATH;

    @Value("${spring.app.seller.url}" + "{sellerId}/products/deleted")
    private String DELETE_PRODUCT_PATH;

    @Value("${spring.app.seller.url}" + "{sellerId}/products/archive")
    private String CHANGE_ARCHIVE_PATH;

    @Value("${spring.app.seller.url}" + "{sellerId}/products/visibility")
    private String CHANGE_VISIBILITY_PATH;

    @Value("${spring.app.seller.url}" + "{sellerId}/products")
    private String GET_PRODUCTS_PATH;

    @Test
    @Transactional
    void shouldChangeProductPriceSellerAndProductFoundAndChanged() throws Exception {
        long updatePrice = 30L;

        serviceTest.changePrice(REAL_SELLER_ID, REAL_PRODUCT_ID, updatePrice,
                CHANGE_PRICE_PATH, status().isOk());

        Long actualPrice = serviceTest.findProductByExternalId(REAL_PRODUCT_ID).price();
        assertEquals(updatePrice, actualPrice, "Received price not as expected");
    }

    @Test
    @Transactional
    void shouldChangeProductPriceSellerNotFoundAndNotChanged() throws Exception {
        long updatePrice = 40L;

        serviceTest.changePrice(UNREAL_SELLER_ID, REAL_PRODUCT_ID, updatePrice,
                CHANGE_PRICE_PATH, status().isOk());

        long actualPrice = serviceTest.findProductByExternalId(REAL_PRODUCT_ID).price();
        assertNotEquals(updatePrice, actualPrice, "Received values are equal, which is not expected");
    }

    private final UUID SELLER_ID = UUID.fromString("301c5370-be41-421e-9b15-f1e80a7098f9");
    private final UUID SELLER_ID_2 = UUID.fromString("301c5370-be41-421e-9b15-f1e80a7098f0");
    List<UUID> PRODUCT_IDS = new ArrayList<>(List.of(
            UUID.fromString("62bce524-29c3-4698-b0d2-7bb76b21e101"),
            UUID.fromString("62bce524-29c3-4698-b0d2-7bb76b21e102"),
            UUID.fromString("62bce524-29c3-4698-b0d2-7bb76b21e103"),
            UUID.fromString("62bce524-29c3-4698-b0d2-7bb76b21e104")
    ));

    @Test
    void shouldSuccessfullyAddProductsToArchive() throws Exception {
        long countBeforeArchived = serviceTest.getArchivedCountBySellerId(SELLER_ID);

        List<UUID> PRODUCTS = PRODUCT_IDS.subList(0, 2);

        serviceTest.moveFromToArchive(SELLER_ID, PRODUCTS, true, CHANGE_ARCHIVE_PATH);

        long actualCount = serviceTest.getArchivedCountBySellerId(SELLER_ID);
        assertEquals(countBeforeArchived + PRODUCTS.size(), actualCount);
    }

    @Test
    void shouldSuccessfullyReturnProductsFromArchive() throws Exception {
        long countBeforeArchived = serviceTest.getArchivedCountBySellerId(SELLER_ID_2);

        List<UUID> PRODUCTS = PRODUCT_IDS.subList(2, 4);

        serviceTest.moveFromToArchive(SELLER_ID_2, PRODUCTS, false, CHANGE_ARCHIVE_PATH);

        long actualCount = serviceTest.getArchivedCountBySellerId(SELLER_ID_2);
        assertEquals(countBeforeArchived - PRODUCTS.size(), actualCount);
    }

    @Test
    void shouldSuccessfullyMakeProductsVisible() throws Exception {
        long countBeforeVisibility = serviceTest.getActualCountAfterDelete(SELLER_ID_2);

        List<UUID> PRODUCTS = PRODUCT_IDS.subList(2, 4);

        serviceTest.changeVisibility(SELLER_ID_2, PRODUCTS, false, CHANGE_VISIBILITY_PATH);

        long actualCount = serviceTest.getArchivedCountBySellerId(SELLER_ID_2);
        assertEquals(0, actualCount);
    }

    @Test
    void shouldSuccessfullyMakeProductsInvisible() throws Exception {
        long countBeforeVisibility = serviceTest.getArchivedCountBySellerId(SELLER_ID);

        serviceTest.changeVisibility(SELLER_ID, PRODUCT_IDS, false, CHANGE_VISIBILITY_PATH);

        long actualCount = serviceTest.getArchivedCountBySellerId(SELLER_ID);
        assertEquals(0, actualCount);

    }


    /**
     * @value <productIds> list elements (2 - exists, 0 - not exists)
     * allProducts = 3, deleted = 2, left = 1
     */
    @Test
    void shouldDeleteListProductBySellerIdSuccessDeleted() throws Exception {
        UUID sellerId = UUID.fromString("301c5370-be41-421e-9b15-f1e80a7074f9");
        List<UUID> productIds = new ArrayList<>(List.of(
                UUID.fromString("301c5370-be41-421e-9b15-f1e80a7074f8"),
                UUID.fromString("301c5370-be41-421e-9b15-f1e80a7074b1")));

        long productsBeforeDeleted = serviceTest.getActualCountAfterDelete(sellerId);

        serviceTest.executeDelete(sellerId, productIds, DELETE_PRODUCT_PATH);

        long expectedBeforeDelete = productsBeforeDeleted - productIds.size(),
                actualCountAfterDelete = serviceTest.getActualCountAfterDelete(sellerId);

        assertEquals(expectedBeforeDelete, actualCountAfterDelete);
    }

    /**
     * @value <productIds> list elements (1 - exists, 1 - not exists)
     */
    @Test
    void shouldDeleteListProductBySellerIdNotAllProductsFound() throws Exception {
        UUID sellerId = UUID.fromString("301c5370-be41-421e-9b15-f1e80a7099f8");
        List<UUID> productIds = new ArrayList<>(List.of(
                UUID.fromString("301c5370-be41-421e-9b15-f1e80a7074b3"),
                UUID.fromString("301c5370-be41-421e-9b15-f1e80a9999c9")));

        serviceTest.executeDelete(sellerId, productIds, DELETE_PRODUCT_PATH);

        long allProductBySellerId = 1, notExists = 1;
        long expectedBeforeDelete = allProductBySellerId - (productIds.size() - notExists),
                actualCountAfterDelete = serviceTest.getActualCountAfterDelete(sellerId);

        assertEquals(expectedBeforeDelete, actualCountAfterDelete);
    }

    /**
     * @value <productIds> list elements (1 - not deleted, 1 already deleted)
     */
    @Test
    void shouldDeleteListProductBySellerIdNotAllProductsCanBeDeleted() throws Exception {
        UUID sellerId = UUID.fromString("301c5370-be41-421e-9b15-f1e80a7099f9");
        List<UUID> productIds = new ArrayList<>(List.of(
                UUID.fromString("301c5370-be41-421e-9b15-f1e80a7074b4"),
                UUID.fromString("301c5370-be41-421e-9b15-f1e80a7074b5")));

        serviceTest.executeDelete(sellerId, productIds, DELETE_PRODUCT_PATH);

        long allProductBySellerId = 2, alreadyDeleted = 1;
        long expectedBeforeDelete = allProductBySellerId - alreadyDeleted - (productIds.size() - alreadyDeleted),
                actualCountAfterDelete = serviceTest.getActualCountAfterDelete(sellerId);

        assertEquals(expectedBeforeDelete, actualCountAfterDelete);
    }
}
