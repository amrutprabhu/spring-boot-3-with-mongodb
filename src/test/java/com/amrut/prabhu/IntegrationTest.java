package com.amrut.prabhu;

import com.amrut.prabhu.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@AutoConfigureMockMvc
public class IntegrationTest {

    @Container
    static GenericContainer mongoDBContainer = new GenericContainer("mongo:4.4.2")
            .withExposedPorts(27017)
            .withEnv("MONGO_INITDB_ROOT_USERNAME", "root")
            .withEnv("MONGO_INITDB_ROOT_PASSWORD", "example");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.host", mongoDBContainer::getContainerIpAddress);
        registry.add("spring.data.mongodb.port", mongoDBContainer::getFirstMappedPort);
        registry.add("spring.data.mongodb.username", () -> "root");
        registry.add("spring.data.mongodb.password", () -> "example");
    }

    @Autowired
    public MockMvc mockMvc;

    @Test
    void storeProductAndRetrieveSuccessfully() throws Exception {

        Product product = new Product();
        product.setProductName("Macbook Pro");
        product.setAttributes(Map.of("Ram", "16GB", "Hard Disk", "512GB"));

        ObjectMapper objectMapper = new ObjectMapper();
        MvcResult mvcResult = mockMvc.perform(post("/product")
                        .content(objectMapper.writeValueAsBytes(product))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.productName", is("Macbook Pro")))
                .andReturn();

        Product response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Product.class);

        mockMvc.perform(get("/product/" + response.getId())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(response.getId())))
                .andExpect(jsonPath("$.productName", is("Macbook Pro")))
                .andExpect(jsonPath("$.attributes['Ram']", is("16GB")))
                .andExpect(jsonPath("$.attributes['Hard Disk']", is("512GB")));

    }

    @Test
    void testFailedProductRetrieval() throws Exception {


        mockMvc.perform(get("/product/1")
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.type", is("https://github.com/amrutprabhu/spring-boot-3-with-mongodb")))
                .andExpect(jsonPath("$.title", is("Not Found")))
                .andExpect(jsonPath("$.detail", is("Product Not found")))
                .andExpect(jsonPath("$.instance", is("/product/1")));

    }

}
