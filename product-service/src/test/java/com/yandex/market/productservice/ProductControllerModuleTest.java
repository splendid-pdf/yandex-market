package com.yandex.market.productservice;

import com.fasterxml.jackson.core.type.TypeReference;
import com.yandex.market.productservice.models.ArchiveTest;
import com.yandex.market.productservice.models.ProductTest;
import com.yandex.market.productservice.models.ProductsTest;
import com.yandex.market.productservice.service.ProductTestService;
import com.yandex.market.util.RestPageImpl;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.UUID;

import static com.yandex.market.productservice.models.Paths.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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

    private final MockMvc mockMvc;

    private final ProductTestService serviceTest;
    private final String AUTH_TOKEN = "eyJraWQiOiJmNTMxMWRhYy0xMzIxLTQxNTItYmQ3NS04YjQ3NjY3Y2E3ZjEiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJzZWxsZXJfZGltYUBnbWFpbC5ydSIsImF1ZCI6ImNsaWVudCIsIm5iZiI6MTY4MjUyNTYwNiwic2NvcGUiOlsib3BlbmlkIl0sImlzcyI6Imh0dHA6Ly81MS4yNTAuMTAyLjEyOjkwMDAiLCJleHAiOjE2ODMxMzA0MDYsImlhdCI6MTY4MjUyNTYwNiwic2VsbGVyLWlkIjoiY2IwNDFkMzEtYTM0NS00ZDgwLTk3MWEtNzBjNDljYmM1YzI4IiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9TRUxMRVIiXX0.H0-mxIai2Lf-aC35V5i4fQdkV4O5LRNzmM_OzdKC_gxLuBX6b-aSfVO8Iwu2NIZPByhh3XD0Ac50W9xsjjvXY9hNs4ol8TqQabENRbsFVmXME2VeFY4xrsO8NIo1JKaI3kJwY8YsvcCb7SKPC-bonaSLLQ_sfvzbQTgnSlziJGXK0lAgnvTJou8Poy-9X63elgXx_uYxQ1NG_cBZInBGfOhA9-OIcwjG_WQnJli_wluyPb-j4C3RFeCQs33zghzZ7TqxtifeuNUZltjXzCwXSXd4qtJbfFbHTKcd8eEEJho9g2VvLd2olNyoRax4eIDneu_5rZg0UmfDWGdcxrzEeA";

    private final String HEADER = "Bearer " + AUTH_TOKEN;

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

    @Test
    @DisplayName("Успешное создание товара с минимальными значениями")
    void shouldCreateProductWithMinimal() throws Exception {

        MvcResult mvcResult = mockMvc.perform(post(PRODUCTS_PATH, REAL_SELLER_ID)
                        .header("Authorization", HEADER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(serviceTest.readFromFile(MIN_PRODUCT_PATH)))
                .andExpect(status().isCreated())
                .andReturn();

        ProductTest product = getProduct(REAL_SELLER_ID, serviceTest.getUuid(mvcResult));

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
    @DisplayName("Создание товара со всеми полями")
    void shouldCreateProductWithMaximal() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post(PRODUCTS_PATH, REAL_SELLER_ID)
                        .header("Authorization", HEADER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(serviceTest.readFromFile(MAX_PRODUCT_PATH)))
                .andExpect(status().isCreated())
                .andReturn();

        ProductTest product = getProduct(REAL_SELLER_ID, serviceTest.getUuid(mvcResult));

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
    void shouldNotCreateProductSellerNotFound() throws Exception {
        mockMvc.perform(post(PRODUCTS_PATH, UNREAL_SELLER_ID)
                        .header("Authorization", HEADER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(serviceTest.readFromFile(MAX_PRODUCT_PATH)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Список товаров успешно получен")
    void shouldCreateProductSuccessful() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                        get(PRODUCTS_PATH, REAL_SELLER_ID, PageRequest.of(0, 20))
                                .header("Authorization", HEADER)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        RestPageImpl<ProductsTest> products = serviceTest.getProductFromMvcResult(
                mvcResult,
                new TypeReference<>() {
                }
        );

        assertNotNull(products);
        assertEquals(REAL_PRODUCTS.size() - ARCHIVED, products.getTotalElements());
    }

    @Test
    @DisplayName("Обновление товара прошло успешно")
    void shouldUpdateProductWithAllInfo() throws Exception {

        mockMvc.perform(put(PRODUCT_PATH, REAL_SELLER_ID, REAL_PRODUCT_ID)
                        .header("Authorization", HEADER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(serviceTest.readFromFile(UPD_PRODUCT_PATH)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Шкаф \"Оврора\" (сталь и дерево)"))
                .andExpect(jsonPath("$.description").value("Супер шкаф, классный, стальной, удобный, также стальная подставка"))
                .andExpect(jsonPath("$.articleFromSeller").value("shkafsuper12"))
                .andExpect(jsonPath("$.brand").value("ООА \"Оврора\""))
                .andExpect(jsonPath("$.price").value(1234L))
                .andExpect(jsonPath("$.count").value(123));
    }

    @Test
    @DisplayName("Список товаров успешно получен")
    void shouldFindPageProductsBySellerIdSuccessfulSearch() throws Exception {

        MvcResult mvcResult = mockMvc.perform(
                        get(PRODUCTS_PATH, REAL_SELLER_ID, PageRequest.of(0, 20))
                                .header("Authorization", HEADER)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        Page<ProductsTest> products = serviceTest.getProductFromMvcResult(mvcResult, new TypeReference<RestPageImpl<ProductsTest>>() {
        });

        long FROM_PRODUCT_LIST = 4 - ARCHIVED;
        assertEquals(FROM_PRODUCT_LIST, products.getTotalElements());
    }

    @Test
    @DisplayName("Архив товаров успешно получен")
    void shouldFindArchivedProductsPageBySellerIdSuccessfulSearch() throws Exception {

        MvcResult mvcResult = mockMvc.perform(
                        get(ARCHIVE_PATH, REAL_SELLER_ID, PageRequest.of(0, 20))
                                .header("Authorization", HEADER)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        Page<ArchiveTest> products = serviceTest.getProductFromMvcResult(mvcResult, new TypeReference<RestPageImpl<ArchiveTest>>() {
        });

        assertEquals(ARCHIVED, products.getTotalElements());
    }

    @Test
    @DisplayName("Успешное получение превью товара")
    void shouldGetProductPreviewsBySellerIdSuccessful() throws Exception {

        MvcResult mvcResult = mockMvc.perform(
                        get(PRODUCT_PATH, REAL_SELLER_ID, REAL_PRODUCT_ID, PageRequest.of(0, 20))
                                .header("Authorization", HEADER)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        ProductTest product = serviceTest.getProductFromMvcResult(mvcResult, new TypeReference<>() {
        });

        Assertions.assertAll(
                () -> assertNotNull(product),
                () -> assertEquals(REAL_PRODUCT_ID, product.id()),
                () -> assertEquals(REAL_SELLER_ID, product.sellerId())
        );
    }

    @Test
    @DisplayName("Не получилось получить превью товара из-за некорректного id продавца")
    void shouldGetProductPreviewsBySellerIdSellerNotFound() throws Exception {

        mockMvc.perform(get(PRODUCT_PATH, UNREAL_SELLER_ID, REAL_PRODUCT_ID, PageRequest.of(0, 20))
                        .header("Authorization", HEADER)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Товар успешно добавлен в архив")
    void shouldChangeIsArchiveFieldSuccessfulAddingToArchive() throws Exception {
        List<UUID> productIds = REAL_PRODUCTS.subList(0, 2);

        mockMvc.perform(patch(ARCHIVE_PATH, REAL_SELLER_ID)
                        .header("Authorization", HEADER)
                        .param("is-archive", "true")
                        .param("product-ids", serviceTest.convertListToParam(productIds))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        for (UUID p : productIds) {
            assertTrue(serviceTest.getProduct(p).isArchived());
        }
    }

    @Test
    @DisplayName("Товар успешно вернули из архива")
    void shouldChangeIsArchiveFieldSuccessfulReturnFromArchive() throws Exception {
        List<UUID> productIds = REAL_PRODUCTS.subList(2, 3);

        mockMvc.perform(patch(ARCHIVE_PATH, REAL_SELLER_ID)
                        .header("Authorization", HEADER)
                        .param("is-archive", "false")
                        .param("product-ids", serviceTest.convertListToParam(productIds))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        for (UUID p : productIds) {
            assertFalse(serviceTest.getProduct(p).isArchived());
        }
    }

    @Test
    @DisplayName("Товар успешно скрыт из списка товаров для пользователей")
    void shouldChangeProductsVisibilitySuccessfulVisibilityEqualsFalse() throws Exception {
        List<UUID> productIds = REAL_PRODUCTS.subList(0, 2);

        mockMvc.perform(patch(CHANGE_VIS_PATH, REAL_SELLER_ID)
                        .header("Authorization", HEADER)
                        .param("is-visible", "false")
                        .param("product-ids", serviceTest.convertListToParam(productIds))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        for (UUID p : productIds) {
            assertFalse(serviceTest.getProduct(p).isVisible());
        }
    }

    @Test
    @DisplayName("Товар успешно вернулся в список товаров для пользователей")
    void shouldChangeProductsVisibilitySuccessfulVisibilityEqualsTrue() throws Exception {
        List<UUID> productIds = REAL_PRODUCTS.subList(3, 4);

        mockMvc.perform(patch(CHANGE_VIS_PATH, REAL_SELLER_ID)
                        .header("Authorization", HEADER)
                        .param("is-visible", "true")
                        .param("product-ids", serviceTest.convertListToParam(productIds))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        for (UUID p : productIds) {
            assertTrue(serviceTest.getProduct(p).isVisible());
        }
    }

    @Test
    @DisplayName("Товар успешно изменился в цене")
    void shouldChangePriceSuccessful() throws Exception {
        long updatePrice = 1234L;

        mockMvc.perform(patch(CHANGE_PRICE_PATH, REAL_SELLER_ID, REAL_PRODUCT_ID)
                        .header("Authorization", HEADER)
                        .param("price", String.valueOf(updatePrice))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        Long actualPrice = serviceTest.getProduct(REAL_PRODUCT_ID).getPrice();
        assertEquals(updatePrice, actualPrice, "Received price not as expected");
    }

    @Test
    @DisplayName("Товар успешно изменился в количестве")
    void shouldChangeCountSuccessful() throws Exception {
        long updateCount = 123L;

        mockMvc.perform(patch(CHANGE_COUNT_PATH, REAL_SELLER_ID, REAL_PRODUCT_ID)
                        .header("Authorization", HEADER)
                        .param("count", String.valueOf(updateCount))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        Long actualCount = serviceTest.getProduct(REAL_PRODUCT_ID).getCount();
        assertEquals(updateCount, actualCount, "Received price not as expected");
    }

    public ProductTest getProduct(UUID sellerId, UUID productId) throws Exception {

        return serviceTest.getProductFromMvcResult(
                mockMvc.perform(get(PRODUCT_PATH, sellerId, productId, PageRequest.of(0, 20))
                                .header("Authorization", HEADER)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn(),
                new TypeReference<>() {
                });
    }
}

