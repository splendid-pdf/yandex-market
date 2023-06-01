package com.yandex.market.productservice;

import com.fasterxml.jackson.core.type.TypeReference;
import com.yandex.market.productservice.dto.response.ProductResponse;
import com.yandex.market.productservice.models.ArchiveResponse;
import com.yandex.market.productservice.models.ProductsResponse;
import com.yandex.market.productservice.models.UserProductPreview;
import com.yandex.market.productservice.service.AbstractTestIntegration;
import com.yandex.market.productservice.service.ProductTestService;
import com.yandex.market.util.RestPageImpl;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
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
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class ProductControllerModuleTest extends AbstractTestIntegration {

    @Autowired
    private MockMvc mockMvc;

    private final ProductTestService serviceTest;

    @Test
    @DisplayName("Продукт не может быть создан, так как продавец не авторизован")
    void shouldNotCreateProductSinceSellerIsNotAuthorized() throws Exception {
        mockMvc.perform(post(PRODUCTS_PATH, REAL_SELLER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(serviceTest.readFromFile(MIN_PRODUCT_PATH)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Transactional
    @DisplayName("Успешное создание товара с минимальными значениями")
    void shouldCreateProductWithMinimal() throws Exception {

        MvcResult mvcResult = mockMvc.perform(post(PRODUCTS_PATH, REAL_SELLER_ID)
                        .with(authentication(serviceTest.getToken(REAL_SELLER_ID.toString())))
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
                        .with(authentication(serviceTest.getToken(REAL_SELLER_ID.toString())))
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
                        .with(authentication(serviceTest.getToken(REAL_SELLER_ID.toString())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(serviceTest.readFromFile(MAX_PRODUCT_PATH)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Список товаров успешно получен")
    void shouldGetProductsPageSuccessful() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                        get(PRODUCTS_PATH, REAL_SELLER_ID, PageRequest.of(0, 20))
                                .with(authentication(serviceTest.getToken(REAL_SELLER_ID.toString())))
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
                        .with(authentication(serviceTest.getToken(REAL_SELLER_ID.toString())))
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
                                .with(authentication(serviceTest.getToken(REAL_SELLER_ID.toString())))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        Page<ProductsResponse> products = serviceTest.getProductFromMvcResult(
                mvcResult,
                new TypeReference<RestPageImpl<ProductsResponse>>() {
                }
        );

        long FROM_PRODUCT_LIST = 4 - ARCHIVED;
        assertEquals(FROM_PRODUCT_LIST, products.getTotalElements());
    }

    @Test
    @DisplayName("Архив товаров успешно получен")
    void shouldFindArchivedProductsPageBySellerIdSuccessfulSearch() throws Exception {

        MvcResult mvcResult = mockMvc.perform(
                        get(ARCHIVE_PATH, REAL_SELLER_ID, PageRequest.of(0, 20))
                                .with(authentication(serviceTest.getToken(REAL_SELLER_ID.toString())))
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
                                .with(authentication(serviceTest.getToken(REAL_SELLER_ID.toString())))
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
                        .with(authentication(serviceTest.getToken(REAL_SELLER_ID.toString())))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    @DisplayName("Товар успешно добавлен в архив")
    void shouldChangeIsArchiveFieldSuccessfulAddingToArchive() throws Exception {
        List<UUID> productIds = REAL_PRODUCTS.subList(0, 2);

        mockMvc.perform(patch(ARCHIVE_PATH, REAL_SELLER_ID)
                        .with(authentication(serviceTest.getToken(REAL_SELLER_ID.toString())))
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
                        .with(authentication(serviceTest.getToken(REAL_SELLER_ID.toString())))
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
                        .with(authentication(serviceTest.getToken(REAL_SELLER_ID.toString())))
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
                        .with(authentication(serviceTest.getToken(REAL_SELLER_ID.toString())))
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
                        .with(authentication(serviceTest.getToken(REAL_SELLER_ID.toString())))
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
                        .with(authentication(serviceTest.getToken(REAL_SELLER_ID.toString())))
                        .param("count", String.valueOf(updateCount))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        Long actualCount = serviceTest.getProduct(REAL_PRODUCT_ID).getCount();
        assertEquals(updateCount, actualCount, "Received price not as expected");
    }

    /*
    TODO: в ожидании изменения логики создания изображения
    @PostMapping("/sellers/{sellerId}/products/{productId}/images")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductImageDto addImage(@PathVariable UUID sellerId,
                                    @PathVariable UUID productId,
                                    @RequestBody @Valid ProductImageDto productImage) {
        return productService.addProductImage(productId, productImage);
    }*/
    @Test
    @Disabled
    void addImageSuccessful() {

    }

    @Test
    @Transactional
    void deleteProductImageSuccessful() throws Exception {
        mockMvc.perform(delete(DEL_IMAGE_PATH, REAL_SELLER_ID)
                        .with(authentication(serviceTest.getToken(REAL_SELLER_ID.toString())))
                        .param("url", "https://random.imagecdn.app/123/123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @Transactional
    @DisplayName("Успешное создание специальной цены для продукта")
    void createSpecialPriceByProductIdSuccessful() throws Exception {
        String response = mockMvc.perform(post(SPECIAL_PRICE_PATH, REAL_SELLER_ID, REAL_PRODUCT_ID)
                        .with(authentication(serviceTest.getToken(REAL_SELLER_ID.toString())))
                        .content(serviceTest.readFromFile(CREATE_SP_PATH))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse().getContentAsString()
                .replaceAll("\"", "");

        assertDoesNotThrow(
                () -> UUID.fromString(response)
        );
    }

    @Test
    @Transactional
    @DisplayName("Успешное обновление специальной цены")
    void updateSpecialPriceSuccessful() throws Exception {
        mockMvc.perform(put(DEFINITE_SP_PATH, REAL_SELLER_ID, REAL_SP_ID)
                        .with(authentication(serviceTest.getToken(REAL_SELLER_ID.toString())))
                        .content(serviceTest.readFromFile(CREATE_SP_PATH))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(String.valueOf(REAL_SP_ID)))
                .andExpect(jsonPath("$.fromDate").value("2026-06-13T10:14:33"))
                .andExpect(jsonPath("$.toDate").value("2026-06-14T10:14:33"))
                .andExpect(jsonPath("$.price").value("2345"));
    }

    @Test
    @Transactional
    void deleteSpecialPriceByIdSuccessful() throws Exception {
        mockMvc.perform(delete(DEFINITE_SP_PATH, REAL_SELLER_ID, REAL_SP_ID)
                        .with(authentication(serviceTest.getToken(REAL_SELLER_ID.toString())))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @Transactional
    void updateProductCharacteristicByIdSuccessful() throws Exception {
        mockMvc.perform(put(CHARACTER_PATH, REAL_SELLER_ID, REAL_CHAR_ID)
                        .with(authentication(serviceTest.getToken(REAL_SELLER_ID.toString())))
                        .content(serviceTest.readFromFile(CREATE_CHAR__PATH))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(String.valueOf(REAL_CHAR_ID)))
                .andExpect(jsonPath("$.name").value("Ширина, см"))
                .andExpect(jsonPath("$.value").value("999.0"));
    }

    @Test
    void getAllProductPreviewsSuccessful() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                        get(PRODUCT_PREVIEW_PATH, PageRequest.of(0, 20))
                                .with(authentication(serviceTest.getToken(REAL_SELLER_ID.toString())))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        RestPageImpl<UserProductPreview> products = serviceTest.getProductFromMvcResult(
                mvcResult,
                new TypeReference<>() {
                }
        );

        assertEquals(REAL_PRODUCTS.size(), products.getTotalElements());
    }

    @Test
    void getProductPreviewsByIdentifiersSuccessful() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                        post(PRODUCT_PREVIEW_PATH, REAL_SELLER_ID, REAL_PRODUCT_ID)
                                .with(authentication(serviceTest.getToken(REAL_SELLER_ID.toString())))
                                .param("product-ids", serviceTest.convertListToParam(REAL_PRODUCTS))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        List<UserProductPreview> products = serviceTest.getProductFromMvcResult(
                mvcResult,
                new TypeReference<>() {
                }
        );

        assertEquals(REAL_PRODUCTS.size(), products.size());
    }

    @Test
    void shouldGetTypeByIdSuccessful() throws Exception {
        mockMvc.perform(get(TYPES_PATH + "/{typeId}", REAL_TYPE_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("301c5370-be41-421e-9b15-f1e80a7079f9"))
                .andExpect(jsonPath("$.name").value("спальня"))
                .andExpect(jsonPath("$.characteristics.size()").value("14"));
    }

    @Test
    void shouldGetTypePreviewsSuccessful() throws Exception {
        mockMvc.perform(get(TYPES_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("спальня"))
                .andExpect(jsonPath("$[0].id").value("301c5370-be41-421e-9b15-f1e80a7079f9"));
    }

    public ProductResponse getProduct(UUID sellerId, UUID productId) throws Exception {
        return serviceTest.getProductFromMvcResult(
                mockMvc.perform(get(PRODUCT_PATH, sellerId, productId, PageRequest.of(0, 20))
                                .with(authentication(serviceTest.getToken(REAL_SELLER_ID.toString())))
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn(),
                new TypeReference<>() {
                });
    }
}

    