package com.yandex.market.productservice;

import com.yandex.market.productservice.dto.response.ProductResponse;
import com.yandex.market.productservice.models.ArchiveTest;
import com.yandex.market.productservice.models.ProductTest;
import com.yandex.market.productservice.models.ProductsTest;
import com.yandex.market.productservice.service.ProductTestService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.jdbc.SqlGroup;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

import static com.yandex.market.util.HttpUtils.PUBLIC_API_V1;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@ActiveProfiles("testcontainers")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@TestPropertySource(locations = "classpath:application-testcontainers.yml")
@SqlConfig(
        transactionMode = SqlConfig.TransactionMode.ISOLATED,
        encoding = "UTF-8"
)
@SqlGroup({
        @Sql(
                scripts = "classpath:db/insert_tests_products.sql",
                executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
        ),
        @Sql(
                scripts = "classpath:db/truncate_products.sql",
                executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
        )
})
class ProductControllerModuleTest {

    private final ProductTestService serviceTest;

    private final UUID REAL_SELLER_ID = UUID.fromString("cb041d31-a345-4d80-971a-70c49cbc5c28");
    private final UUID UNREAL_SELLER_ID = UUID.fromString("cb041d31-a345-4d80-971a-70c49cbc5c99");
    private final UUID REAL_PRODUCT_ID = UUID.fromString("0a751cc5-4325-444e-ac1d-df48c678d7b1");

    private final List<UUID> REAL_PRODUCTS = List.of(
            REAL_PRODUCT_ID,
            UUID.fromString("0a751cc5-4325-444e-ac1d-df48c678d7b2"),
            UUID.fromString("0a751cc5-4325-444e-ac1d-df48c678d7b3"),
            UUID.fromString("0a751cc5-4325-444e-ac1d-df48c678d7b4")
    );

    private final long ARCHIVED = 1;
    private final long FROM_PRODUCT_LIST = 4 - ARCHIVED;

    private final String SELLER_PATH = "/" + PUBLIC_API_V1 + "/sellers/{sellerId}/";
    private final String SELLER_PRODUCT_PATH = SELLER_PATH + "products";
    private final String PRODUCT_PATH = SELLER_PATH + "products/{productId}";
    private final String SELLER_ARCHIVED_PATH = SELLER_PATH + "archive/products";
    private final String CHANGE_PRICE_PATH = SELLER_PATH + "products/{productId}/price";
    private final String CHANGE_COUNT_PATH = SELLER_PATH + "products/{productId}/count";
    private final String CHANGE_VISIBILITY_PATH = SELLER_PATH + "products/visibility";

    private final String PATH_TO_MINIMAL_PRODUCT = "src/test/resources/json/create_product_with_minimal.json";
    private final String PATH_TO_MAX_PRODUCT = "src/test/resources/json/create_product_with_max_fields.json";
    private final String PATH_TO_UPDATE_PRODUCT = "src/test/resources/json/update_product_name_and_desc.json";

    @Test
    @DisplayName("Успешное создание товара с минимальными значениями")
    void shouldCreateProductWithMinimal() throws Exception {
        UUID productId = serviceTest.createProduct(
                SELLER_PRODUCT_PATH,
                REAL_SELLER_ID,
                Files.readString(Path.of(PATH_TO_MINIMAL_PRODUCT)), status().isCreated());

        ProductTest product = serviceTest.getProduct(PRODUCT_PATH, REAL_SELLER_ID, productId, status().isOk());

        assertAll(
                () -> assertNotNull(product),
                () -> assertEquals("Шкаф \"Супер классный\" (дерево)", product.name()),
                () -> assertEquals(
                        UUID.fromString("301c5370-be41-421e-9b15-f1e80a7079f9"),
                        product.type().id()
                )
        );
    }

    @Test
    @DisplayName("Обновление товара со всеми полями")
    void shouldCreateProductWithMaximal() throws Exception {
        UUID productId = serviceTest.createProduct(
                SELLER_PRODUCT_PATH,
                REAL_SELLER_ID,
                Files.readString(Path.of(PATH_TO_MAX_PRODUCT)),
                status().isCreated());

        ProductTest product = serviceTest.getProduct(PRODUCT_PATH, REAL_SELLER_ID, productId, status().isOk());

        assertAll(
                () -> assertNotNull(product),
                () -> assertNotNull(product.sellerId()),
                () -> assertEquals("Шкаф \"Аврора\" (сталь)", product.name()),
                () -> assertEquals(
                        UUID.fromString("301c5370-be41-421e-9b15-f1e80a7079f9"),
                        product.type().id()
                ),
                () -> assertEquals("Супер шкаф, классный, стальной, удобный", product.description()),
                () -> assertEquals("ООО \"Аврора\"", product.brand()),
                () -> assertEquals(UUID.fromString("301c5370-be41-421e-9b15-f1e80a7079f9"), product.type().id()),
                () -> assertTrue(product.characteristics().size() >= 10),
                () -> assertEquals(3, product.images().size())
        );
    }

    @Test
    @DisplayName("Товар не может быть создан, так как некорректный id продавца")
    void shouldCreateProductSellerNotFound() throws Exception {
        UUID productId = serviceTest.createProduct(
                SELLER_PRODUCT_PATH,
                UNREAL_SELLER_ID,
                Files.readString(Path.of(PATH_TO_MAX_PRODUCT)),
                status().isForbidden());
        assertNull(productId);
    }

    @Test
    @DisplayName("Товар успешно создан")
    void shouldCreateProductSuccessful() throws Exception {
        Page<ProductsTest> products = serviceTest.getProducts(SELLER_PRODUCT_PATH, REAL_SELLER_ID);
        assertNotNull(products);
        assertEquals(REAL_PRODUCTS.size() - ARCHIVED, products.getTotalElements());
    }

    @Test
    @DisplayName("Обновление товара прошло успешно")
    void shouldUpdateProductWithAllInfo() throws Exception {
        ProductResponse productResponse = serviceTest.updateProduct(
                PRODUCT_PATH,
                REAL_SELLER_ID,
                REAL_PRODUCT_ID,
                Files.readString(Path.of(PATH_TO_UPDATE_PRODUCT)),
                status().isOk()
        );

        assertAll(
                () -> assertNotNull(productResponse),
                () -> assertEquals("Шкаф \"Оврора\" (сталь и дерево)", productResponse.name()),
                () -> assertEquals(
                        "Супер шкаф, классный, стальной, удобный, также стальная подставка",
                        productResponse.description()
                ),
                () -> assertEquals("shkafsuper12", productResponse.articleFromSeller()),
                () -> assertEquals("ООА \"Оврора\"", productResponse.brand()),
                () -> assertEquals(1234L, productResponse.price()),
                () -> assertEquals(123, productResponse.count())
        );
    }

    @Test
    @DisplayName("Список товаров успешно получен")
    void shouldFindPageProductsBySellerIdSuccessfulSearch() throws Exception {
        Page<ProductsTest> products = serviceTest.getProducts(SELLER_PRODUCT_PATH, REAL_SELLER_ID);
        assertNotNull(products);
        assertEquals(FROM_PRODUCT_LIST, products.getTotalElements());
    }

    @Test
    @DisplayName("Архив товаров успешно получен")
    void shouldFindArchivedProductsPageBySellerIdSuccessfulSearch() throws Exception {
        Page<ArchiveTest> products = serviceTest.getArchivedProducts(SELLER_ARCHIVED_PATH, REAL_SELLER_ID);
        assertNotNull(products);
        assertEquals(ARCHIVED, products.getTotalElements());
    }

    @Test
    @DisplayName("Успешное получение превью товара")
    void shouldGetProductPreviewsBySellerIdSuccessful() throws Exception {
        ProductTest product = serviceTest.getProduct(
                PRODUCT_PATH,
                REAL_SELLER_ID,
                REAL_PRODUCT_ID,
                status().isOk());
        Assertions.assertAll(
                () -> assertNotNull(product),
                () -> assertEquals(REAL_PRODUCT_ID, product.id()),
                () -> assertEquals(REAL_SELLER_ID, product.sellerId())
        );
    }

    @Test
    @DisplayName("Не получилось получить превью товара из-за некорректного id продавца")
    void shouldGetProductPreviewsBySellerIdSellerNotFound() throws Exception {
        ProductTest product = serviceTest.getProduct(
                PRODUCT_PATH,
                UNREAL_SELLER_ID,
                REAL_PRODUCT_ID,
                status().isForbidden());
        assertNull(product);
    }

    @Test
    @DisplayName("Товар успешно добавлен в архив")
    void shouldChangeIsArchiveFieldSuccessfulAddingToArchive() throws Exception {
        List<UUID> productIds = REAL_PRODUCTS.subList(0, 2);
        serviceTest.changeArchive(
                SELLER_ARCHIVED_PATH,
                REAL_SELLER_ID,
                productIds,
                true);

        for (UUID p : productIds) {
            assertTrue(serviceTest.getProduct(p).isArchived());
        }
    }

    @Test
    @DisplayName("Товар успешно вернули из архива")
    void shouldChangeIsArchiveFieldSuccessfulReturnFromArchive() throws Exception {
        List<UUID> productIds = REAL_PRODUCTS.subList(2, 3);
        serviceTest.changeArchive(
                SELLER_ARCHIVED_PATH,
                REAL_SELLER_ID,
                productIds,
                false);

        for (UUID p : productIds) {
            assertFalse(serviceTest.getProduct(p).isArchived());
        }
    }

    @Test
    @DisplayName("Товар успешно скрыт из списка товаров для пользователей")
    void shouldChangeProductsVisibilitySuccessfulVisibilityEqualsFalse() throws Exception {
        List<UUID> productIds = REAL_PRODUCTS.subList(0, 2);
        serviceTest.changeVisible(
                CHANGE_VISIBILITY_PATH,
                REAL_SELLER_ID,
                productIds,
                false);

        for (UUID p : productIds) {
            assertFalse(serviceTest.getProduct(p).isVisible());
        }
    }

    @Test
    @DisplayName("Товар успешно вернулся в список товаров для пользователей")
    void shouldChangeProductsVisibilitySuccessfulVisibilityEqualsTrue() throws Exception {
        List<UUID> productIds = REAL_PRODUCTS.subList(3, 4);
        serviceTest.changeVisible(
                CHANGE_VISIBILITY_PATH,
                REAL_SELLER_ID,
                productIds,
                true);

        for (UUID p : productIds) {
            assertTrue(serviceTest.getProduct(p).isVisible());
        }
    }

    @Test
    @DisplayName("Товар успешно изменился в цене")
    void shouldChangePriceSuccessful() throws Exception {
        long updatePrice = 1234L;

//        serviceTest.changePrice(CHANGE_PRICE_PATH, REAL_SELLER_ID, REAL_PRODUCT_ID, updatePrice);
        serviceTest.changeProductParameters(
                CHANGE_PRICE_PATH,
                REAL_SELLER_ID,
                REAL_PRODUCT_ID,
                "price",
                String.valueOf(updatePrice));

        Long actualPrice = serviceTest.getProduct(REAL_PRODUCT_ID).getPrice();
        assertEquals(updatePrice, actualPrice, "Received price not as expected");
    }

    @Test
    @DisplayName("Товар успешно изменился в количестве")
    void shouldChangeCountSuccessful() throws Exception {
        long updateCount = 123L;

        serviceTest.changeProductParameters(
                CHANGE_COUNT_PATH,
                REAL_SELLER_ID,
                REAL_PRODUCT_ID,
                "count",
                String.valueOf(updateCount));

        Long actualCount = serviceTest.getProduct(REAL_PRODUCT_ID).getCount();
        assertEquals(updateCount, actualCount, "Received price not as expected");
    }
}

