//package com.yandex.market.shopservice.controllers.shop.tests;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.github.cliftonlabs.json_simple.JsonObject;
//import com.github.cliftonlabs.json_simple.Jsoner;
//import com.yandex.market.shopservice.controllers.shop.ShopSystemController;
//import org.junit.Before;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.jdbc.Sql;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import java.io.Reader;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@AutoConfigureMockMvc
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@Sql(value = {"classpath:db/tests/insert_data_into_shop_system.sql"},
//        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
//@Sql(value = {"classpath:db/tests/truncate_shop_system.sql"},
//        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
//class ShopSystemControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Value("/" + "${spring.application.url.shop}")
//    private String PRIVATE_API;
//    private final String ENTRY_PATH = "src/test/java/com/yandex/market/shopservice/controllers/shop/files/";
//
//    @Before
//    public void setup() {
//        this.mockMvc = MockMvcBuilders.standaloneSetup(ShopSystemController.class).build();
//    }
//
//    @Test
//    void createNewShopSystem_correctSuccess() throws Exception {
//        Reader reader = Files.newBufferedReader(
//                Paths.get(ENTRY_PATH + "shop_system_correct_data.json"));
//        JsonObject parser = (JsonObject) Jsoner.deserialize(reader);
//
//        String jsonRequest = objectMapper.writeValueAsString(parser);
//        mockMvc.perform(post(PRIVATE_API)
//                        .content(jsonRequest)
//                        .contentType(MediaType.APPLICATION_JSON_VALUE))
//                .andExpect(status().isCreated());
//    }
//
//    @Test
//    void createNewShopSystem_minimalCorrectSuccess() throws Exception {
//        Reader reader = Files.newBufferedReader(
//                Paths.get(ENTRY_PATH + "shop_system_minimal_correct_data.json"));
//
//        String jsonRequest = objectMapper.writeValueAsString(Jsoner.deserialize(reader));
//        mockMvc.perform(post(PRIVATE_API)
//                        .content(jsonRequest)
//                        .contentType(MediaType.APPLICATION_JSON_VALUE))
//                .andExpect(status().isCreated());
//    }
//
//    @Test
//    void createNewShopSystem_emptyData() throws Exception {
//        Reader reader = Files.newBufferedReader(
//                Paths.get(ENTRY_PATH + "shop_system_empty_data.json"));
//        JsonObject parser = (JsonObject) Jsoner.deserialize(reader);
//
//        String jsonRequest = objectMapper.writeValueAsString(parser);
//        mockMvc.perform(post(PRIVATE_API)
//                        .content(jsonRequest)
//                        .contentType(MediaType.APPLICATION_JSON_VALUE))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    void createNewShopSystem_incorrectFailed() throws Exception {
//        Reader reader = Files.newBufferedReader(
//                Paths.get(ENTRY_PATH + "shop_system_incorrect_surrort.json"));
//        JsonObject parser = (JsonObject) Jsoner.deserialize(reader);
//
//        String jsonRequest = objectMapper.writeValueAsString(parser);
//        mockMvc.perform(post(PRIVATE_API)
//                        .content(jsonRequest)
//                        .contentType(MediaType.APPLICATION_JSON_VALUE))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    void getShopSystemByExternalId_oneFound() {
//    }
//
//    @Test
//    void getShopSystemByExternalId_notFound() {
//
//    }
//
//    @Test
//    void deleteSystemShopByExternalId() {
//    }
//}