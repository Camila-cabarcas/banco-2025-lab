package com.ejemplo.banco;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private void createCustomer(String account, double balance) throws Exception {
        String json = "{\"firstName\":\"Ana\",\"lastName\":\"Perez\",\"accountNumber\":\""
                + account + "\",\"balance\":" + balance + "}";
        mockMvc.perform(post("/api/customers").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber").value(account));
    }

    @Test
    void customersEndpointsWork() throws Exception {
        createCustomer("API-1", 300.0);

        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void transactionsEndpointsWork() throws Exception {
        createCustomer("API-2", 300.0);
        createCustomer("API-3", 0.0);

        String transfer = "{\"senderAccountNumber\":\"API-2\",\"receiverAccountNumber\":\"API-3\",\"amount\":100.0}";
        mockMvc.perform(post("/api/transactions").contentType(MediaType.APPLICATION_JSON).content(transfer))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(100.0));

        mockMvc.perform(get("/api/transactions"))
                .andExpect(status().isOk());
    }
}
