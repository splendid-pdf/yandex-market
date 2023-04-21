//package com.yandex.market.productservice;
//
//import com.yandex.market.productservice.service.ProductSellerServiceTest;
//import lombok.RequiredArgsConstructor;
//import org.junit.jupiter.api.Disabled;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.TestPropertySource;
//import org.springframework.test.context.jdbc.Sql;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//
//import static com.yandex.market.util.HttpUtils.PUBLIC_API_V1;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotEquals;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@ActiveProfiles("test")
//@TestPropertySource(locations = "classpath:application-test.yaml")
//@RequiredArgsConstructor(onConstructor_ = {@Autowired})
//@Sql(scripts = {"classpath:db/insert_tests_fields.sql", "classpath:db/insert_types.sql"},
//        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
//public class SellerControllerTest {
//    private final ProductSellerServiceTest serviceTest;
//
//    private final UUID REAL_SELLER_ID = UUID.fromString("81fe9426-2891-4f13-a3b7-6e238fb731b3");
//
//    private final UUID REAL_PRODUCT_ID = UUID.fromString("62bce524-29c3-4698-b0d2-7bb76b21e723");
//
//    private final UUID UNREAL_SELLER_ID = UUID.fromString("00678201-f3c8-4d5c-a628-2344eef50c61");
//
//    private final String SELLER_PATH = "/" + PUBLIC_API_V1 + "/sellers/",
//            CHANGE_PRICE_PATH = SELLER_PATH + "/{sellerId}/products/{productId}/price",
//            CHANGE_COUNT_PATH = SELLER_PATH + "/{sellerId}/products/{productId}/count",
//            DELETE_PRODUCT_PATH = SELLER_PATH + "/{sellerId}/products",
//            CHANGE_ARCHIVE_PATH = SELLER_PATH + "/{sellerId}/products/archive",
//            CHANGE_VISIBILITY_PATH = SELLER_PATH + "/{sellerId}/products/visibility";
//
//    @Test
//    @Sql({"", ""})
//    @Transactional
//    void shouldChangeProductPriceSellerAndProductFoundAndChanged() throws Exception {
//        long updatePrice = 30L;
//
//        serviceTest.changePrice(REAL_SELLER_ID, REAL_PRODUCT_ID, updatePrice, CHANGE_PRICE_PATH);
//
//        Long actualPrice = serviceTest.findProductByExternalId(REAL_PRODUCT_ID).price();
//        assertEquals(updatePrice, actualPrice, "Received price not as expected");
//    }
//
//    @Test
//    @Transactional
//    void shouldChangeProductPriceSellerNotFoundAndNotChanged() throws Exception {
//        long updatePrice = 40L;
//
//        serviceTest.changePrice(UNREAL_SELLER_ID, REAL_PRODUCT_ID, updatePrice, CHANGE_PRICE_PATH);
//
//        long actualPrice = serviceTest.findProductByExternalId(REAL_PRODUCT_ID).price();
//        assertNotEquals(updatePrice, actualPrice, "Received values are equal, which is not expected");
//    }
//
//    @Test
//    @Transactional
//    void shouldChangeProductCountSellerAndProductFoundAndChanged() throws Exception {
//        long updateCount = 25L;
//
//        serviceTest.changeCount(REAL_SELLER_ID, REAL_PRODUCT_ID, updateCount, CHANGE_COUNT_PATH);
//
//        Long actualCount = serviceTest.findProductByExternalId(REAL_PRODUCT_ID).count();
//        assertEquals(updateCount, actualCount, "Received count not as expected");
//    }
//
//    @Test
//    @Transactional
//    void shouldChangeProductCountSellerNotFoundAndNotChanged() throws Exception {
//        long updateCount = 35L;
//
//        serviceTest.changeCount(UNREAL_SELLER_ID, REAL_PRODUCT_ID, updateCount, CHANGE_COUNT_PATH);
//
//        long actualCount = serviceTest.findProductByExternalId(REAL_PRODUCT_ID).count();
//        assertNotEquals(updateCount, actualCount, "Received values are equal, which is not expected");
//    }
//
//    private final UUID SELLER_ID = UUID.fromString("301c5370-be41-421e-9b15-f1e80a7098f9");
//    private final UUID SELLER_ID_2 = UUID.fromString("301c5370-be41-421e-9b15-f1e80a7098f0");
//    List<UUID> PRODUCT_IDS = new ArrayList<>(List.of(
//            UUID.fromString("62bce524-29c3-4698-b0d2-7bb76b21e101"),
//            UUID.fromString("62bce524-29c3-4698-b0d2-7bb76b21e102"),
//            UUID.fromString("62bce524-29c3-4698-b0d2-7bb76b21e103"),
//            UUID.fromString("62bce524-29c3-4698-b0d2-7bb76b21e104")
//    ));
//
//    @Test
//    void shouldSuccessfullyAddProductsToArchive() throws Exception {
//        long countBeforeArchived = serviceTest.getArchivedCountBySellerId(SELLER_ID);
//
//        List<UUID> PRODUCTS = PRODUCT_IDS.subList(0, 2);
//
//        serviceTest.moveFromToArchive(SELLER_ID, PRODUCTS, true, CHANGE_ARCHIVE_PATH);
//
//        long actualCount = serviceTest.getArchivedCountBySellerId(SELLER_ID);
//        assertEquals(countBeforeArchived + PRODUCTS.size(), actualCount);
//    }
//
//    @Test
//    void shouldSuccessfullyReturnProductsFromArchive() throws Exception {
//        long countBeforeArchived = serviceTest.getArchivedCountBySellerId(SELLER_ID_2);
//
//        List<UUID> PRODUCTS = PRODUCT_IDS.subList(2, 4);
//
//        serviceTest.moveFromToArchive(SELLER_ID_2, PRODUCTS, false, CHANGE_ARCHIVE_PATH);
//
//        long actualCount = serviceTest.getArchivedCountBySellerId(SELLER_ID_2);
//        assertEquals(countBeforeArchived - PRODUCTS.size(), actualCount);
//    }
//
//    /**
//     * @value <productIds> list elements (2 - exists, 0 - not exists)
//     * allProducts = 3, deleted = 2, left = 1
//     */
//    @Test
//    @Disabled
//    void shouldDeleteListProductBySellerIdSuccessDeleted() throws Exception {
//        UUID sellerId = UUID.fromString("301c5370-be41-421e-9b15-f1e80a7074f9");
//        List<UUID> productIds = new ArrayList<>(List.of(
//                UUID.fromString("301c5370-be41-421e-9b15-f1e80a7074f8"),
//                UUID.fromString("301c5370-be41-421e-9b15-f1e80a7074b1")));
//
//        long productsBeforeDeleted = serviceTest.getActualCountAfterDelete(sellerId);
//
//        serviceTest.executeDelete(sellerId, productIds, DELETE_PRODUCT_PATH);
//
//        long expectedBeforeDelete = productsBeforeDeleted - productIds.size(),
//                actualCountAfterDelete = serviceTest.getActualCountAfterDelete(sellerId);
//
//        assertEquals(expectedBeforeDelete, actualCountAfterDelete);
//    }
//
//    /**
//     * @value <productIds> list elements (1 - exists, 1 - not exists)
//     */
//    @Test
//    @Disabled
//    void shouldDeleteListProductBySellerIdNotAllProductsFound() throws Exception {
//        UUID sellerId = UUID.fromString("301c5370-be41-421e-9b15-f1e80a7099f8");
//        List<UUID> productIds = new ArrayList<>(List.of(
//                UUID.fromString("301c5370-be41-421e-9b15-f1e80a7074b3"),
//                UUID.fromString("301c5370-be41-421e-9b15-f1e80a9999c9")));
//
//        serviceTest.executeDelete(sellerId, productIds, DELETE_PRODUCT_PATH);
//
//        long allProductBySellerId = 1, notExists = 1;
//        long expectedBeforeDelete = allProductBySellerId - (productIds.size() - notExists),
//                actualCountAfterDelete = serviceTest.getActualCountAfterDelete(sellerId);
//
//        assertEquals(expectedBeforeDelete, actualCountAfterDelete);
//    }
//
//    /**
//     * @value <productIds> list elements (1 - not deleted, 1 already deleted)
//     */
//    @Test
//    @Disabled
//    void shouldDeleteListProductBySellerIdNotAllProductsCanBeDeleted() throws Exception {
//        UUID sellerId = UUID.fromString("301c5370-be41-421e-9b15-f1e80a7099f9");
//        List<UUID> productIds = new ArrayList<>(List.of(
//                UUID.fromString("301c5370-be41-421e-9b15-f1e80a7074b4"),
//                UUID.fromString("301c5370-be41-421e-9b15-f1e80a7074b5")));
//
//        serviceTest.executeDelete(sellerId, productIds, DELETE_PRODUCT_PATH);
//
//        long allProductBySellerId = 2, alreadyDeleted = 1;
//        long expectedBeforeDelete = allProductBySellerId - alreadyDeleted - (productIds.size() - alreadyDeleted),
//                actualCountAfterDelete = serviceTest.getActualCountAfterDelete(sellerId);
//
//        assertEquals(expectedBeforeDelete, actualCountAfterDelete);
//    }
//}
