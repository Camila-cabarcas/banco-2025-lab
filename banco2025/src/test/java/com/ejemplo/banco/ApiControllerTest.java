package com.ejemplo.banco;

import com.ejemplo.banco.DTO.CustomerDTO;
import com.ejemplo.banco.controller.CustomerController;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerController customerController;

    private long createCustomer(String account, double balance) throws Exception {
        String json = "{\"firstName\":\"Ana\",\"lastName\":\"Perez\",\"accountNumber\":\""
                + account + "\",\"balance\":" + balance + "}";
        String response = mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber").value(account))
                .andReturn().getResponse().getContentAsString();
        return ((Number) JsonPath.read(response, "$.id")).longValue();
    }

    private long transfer(String from, String to, double amount) throws Exception {
        String json = "{\"senderAccountNumber\":\"" + from + "\",\"receiverAccountNumber\":\"" + to
                + "\",\"amount\":" + amount + "}";
        String response = mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(amount))
                .andReturn().getResponse().getContentAsString();
        return ((Number) JsonPath.read(response, "$.id")).longValue();
    }

    @Test
    void customersEndpointsWork() throws Exception {
        long id = createCustomer("API-1", 300.0);

        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        mockMvc.perform(get("/api/customers/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber").value("API-1"))
                .andExpect(jsonPath("$.balance").value(300.0));
    }

    @Test
    void createCustomerWithoutBalanceIsRejected() {
        CustomerDTO withoutBalance = new CustomerDTO(null, "Ana", "Perez", "API-0", null);

        assertThrows(IllegalArgumentException.class, () -> customerController.createCustomer(withoutBalance));
    }

    @Test
    void transactionsEndpointsWork() throws Exception {
        createCustomer("API-2", 300.0);
        createCustomer("API-3", 0.0);

        long id = transfer("API-2", "API-3", 100.0);

        mockMvc.perform(get("/api/transactions"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/transactions/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.senderAccountNumber").value("API-2"));

        String update = "{\"senderAccountNumber\":\"API-2\",\"receiverAccountNumber\":\"API-3\",\"amount\":150.0}";
        mockMvc.perform(put("/api/transactions/" + id)
                        .contentType(MediaType.APPLICATION_JSON).content(update))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(150.0));

        mockMvc.perform(delete("/api/transactions/" + id))
                .andExpect(status().isNoContent());
    }
}
