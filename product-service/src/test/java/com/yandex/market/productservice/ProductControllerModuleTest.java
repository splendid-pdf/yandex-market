package com.yandex.market.productservice;

import com.fasterxml.jackson.core.type.TypeReference;
import com.yandex.market.productservice.models.ArchiveResponse;
import com.yandex.market.productservice.models.ProductResponse;
import com.yandex.market.productservice.models.ProductsResponse;
import com.yandex.market.productservice.service.AbstractTestIntegration;
import com.yandex.market.productservice.service.ProductTestService;
import com.yandex.market.util.RestPageImpl;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.UUID;

import static com.yandex.market.productservice.models.Environment.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class ProductControllerModuleTest extends AbstractTestIntegration {

    @Autowired
    private MockMvc mockMvc;

    private final ProductTestService serviceTest;

    @Test
    @Transactional
    @DisplayName("Успешное создание товара с минимальными значениями")
    void shouldCreateProductWithMinimal() throws Exception {

        MvcResult mvcResult = mockMvc.perform(post(PRODUCTS_PATH, REAL_SELLER_ID)
                        .header("Authorization", AUTH_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(serviceTest.readFromFile(MIN_PRODUCT_PATH)))
                .andExpect(status().isCreated())
                .andReturn();

        ProductResponse product = getProduct(REAL_SELLER_ID, serviceTest.getUuid(mvcResult));

        assertAll(
                () -> assertNotNull(product),
                () -> assertEquals("Шкаф \"Супер классный\" (дерево)", product.name()),
                () -> assertEquals(REAL_TYPE_ID, product.type().id())
        );
    }

    @Test
    @Transactional
    @DisplayName("Создание товара со всеми полями")
    void shouldCreateProductWithMaximal() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post(PRODUCTS_PATH, REAL_SELLER_ID)
                        .header("Authorization", AUTH_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(serviceTest.readFromFile(MAX_PRODUCT_PATH)))
                .andExpect(status().isCreated())
                .andReturn();

        ProductResponse product = getProduct(REAL_SELLER_ID, serviceTest.getUuid(mvcResult));

        assertAll(
                () -> assertNotNull(product),
                () -> assertNotNull(product.sellerId()),
                () -> assertEquals("Шкаф \"Аврора\" (сталь)", product.name()),
                () -> assertEquals(REAL_TYPE_ID, product.type().id()),
                () -> assertEquals("Супер шкаф, классный, стальной, удобный", product.description()),
                () -> assertEquals("ООО \"Аврора\"", product.brand()),
                () -> assertEquals(14, product.characteristics().size()),
                () -> assertEquals(3, product.images().size())
        );
    }

    @Test
    @Transactional
    @DisplayName("Товар не может быть создан, так как некорректный id продавца")
    void shouldNotCreateProductSellerNotFound() throws Exception {
        mockMvc.perform(post(PRODUCTS_PATH, UNREAL_SELLER_ID)
                        .header("Authorization", AUTH_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(serviceTest.readFromFile(MAX_PRODUCT_PATH)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Список товаров успешно получен")
    void shouldGetProductsPageSuccessful() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                        get(PRODUCTS_PATH, REAL_SELLER_ID, PageRequest.of(0, 20))
                                .header("Authorization", AUTH_TOKEN)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        RestPageImpl<ProductsResponse> products = serviceTest.getProductFromMvcResult(
                mvcResult,
                new TypeReference<>() {
                }
        );

        assertNotNull(products);
        assertEquals(REAL_PRODUCTS.size() - ARCHIVED, products.getTotalElements());
    }

    @Test
    @Transactional
    @DisplayName("Обновление товара прошло успешно")
    void shouldUpdateProductWithAllInfo() throws Exception {

        mockMvc.perform(put(PRODUCT_PATH, REAL_SELLER_ID, REAL_PRODUCT_ID)
                        .header("Authorization", AUTH_TOKEN)
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
                                .header("Authorization", AUTH_TOKEN)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        Page<ProductsResponse> products = serviceTest.getProductFromMvcResult(mvcResult, new TypeReference<RestPageImpl<ProductsResponse>>() {
        });

        long FROM_PRODUCT_LIST = 4 - ARCHIVED;
        assertEquals(FROM_PRODUCT_LIST, products.getTotalElements());
    }

    @Test
    @DisplayName("Архив товаров успешно получен")
    void shouldFindArchivedProductsPageBySellerIdSuccessfulSearch() throws Exception {

        MvcResult mvcResult = mockMvc.perform(
                        get(ARCHIVE_PATH, REAL_SELLER_ID, PageRequest.of(0, 20))
                                .header("Authorization", AUTH_TOKEN)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        Page<ArchiveResponse> products = serviceTest.getProductFromMvcResult(mvcResult, new TypeReference<RestPageImpl<ArchiveResponse>>() {
        });

        assertEquals(ARCHIVED, products.getTotalElements());
    }

    @Test
    @DisplayName("Успешное получение превью товара")
    void shouldGetProductPreviewsBySellerIdSuccessful() throws Exception {

        MvcResult mvcResult = mockMvc.perform(
                        get(PRODUCT_PATH, REAL_SELLER_ID, REAL_PRODUCT_ID, PageRequest.of(0, 20))
                                .header("Authorization", AUTH_TOKEN)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        ProductResponse product = serviceTest.getProductFromMvcResult(mvcResult, new TypeReference<>() {
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
                        .header("Authorization", AUTH_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    @DisplayName("Товар успешно добавлен в архив")
    void shouldChangeIsArchiveFieldSuccessfulAddingToArchive() throws Exception {
        List<UUID> productIds = REAL_PRODUCTS.subList(0, 2);

        mockMvc.perform(patch(ARCHIVE_PATH, REAL_SELLER_ID)
                        .header("Authorization", AUTH_TOKEN)
                        .param("is-archive", "true")
                        .param("product-ids", serviceTest.convertListToParam(productIds))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        for (UUID p : productIds) {
            assertTrue(serviceTest.getProduct(p).isArchived());
        }
    }

    @Test
    @Transactional
    @DisplayName("Товар успешно вернули из архива")
    void shouldChangeIsArchiveFieldSuccessfulReturnFromArchive() throws Exception {
        List<UUID> productIds = REAL_PRODUCTS.subList(2, 3);

        mockMvc.perform(patch(ARCHIVE_PATH, REAL_SELLER_ID)
                        .header("Authorization", AUTH_TOKEN)
                        .param("is-archive", "false")
                        .param("product-ids", serviceTest.convertListToParam(productIds))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        for (UUID p : productIds) {
            assertFalse(serviceTest.getProduct(p).isArchived());
        }
    }

    @Test
    @Transactional
    @DisplayName("Товар успешно скрыт из списка товаров для пользователей")
    void shouldChangeProductsVisibilitySuccessfulVisibilityEqualsFalse() throws Exception {
        List<UUID> productIds = REAL_PRODUCTS.subList(0, 2);

        mockMvc.perform(patch(CHANGE_VIS_PATH, REAL_SELLER_ID)
                        .header("Authorization", AUTH_TOKEN)
                        .param("is-visible", "false")
                        .param("product-ids", serviceTest.convertListToParam(productIds))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        for (UUID p : productIds) {
            assertFalse(serviceTest.getProduct(p).isVisible());
        }
    }

    @Test
    @Transactional
    @DisplayName("Товар успешно вернулся в список товаров для пользователей")
    void shouldChangeProductsVisibilitySuccessfulVisibilityEqualsTrue() throws Exception {
        List<UUID> productIds = REAL_PRODUCTS.subList(3, 4);

        mockMvc.perform(patch(CHANGE_VIS_PATH, REAL_SELLER_ID)
                        .header("Authorization", AUTH_TOKEN)
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
                        .header("Authorization", AUTH_TOKEN)
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
                        .header("Authorization", AUTH_TOKEN)
                        .param("count", String.valueOf(updateCount))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        Long actualCount = serviceTest.getProduct(REAL_PRODUCT_ID).getCount();
        assertEquals(updateCount, actualCount, "Received price not as expected");
    }



    public ProductResponse getProduct(UUID sellerId, UUID productId) throws Exception {

        return serviceTest.getProductFromMvcResult(
                mockMvc.perform(get(PRODUCT_PATH, sellerId, productId, PageRequest.of(0, 20))
                                .header("Authorization", AUTH_TOKEN)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn(),
                new TypeReference<>() {
                });
    }
}

