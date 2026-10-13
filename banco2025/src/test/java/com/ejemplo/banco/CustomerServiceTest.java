package com.ejemplo.banco;

import com.ejemplo.banco.DTO.CustomerDTO;
import com.ejemplo.banco.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CustomerServiceTest {

    @Autowired
    private CustomerService customerService;

    private CustomerDTO newCustomer(String account, double balance) {
        return new CustomerDTO(null, "Ana", "Perez", account, balance);
    }

    @Test
    void createCustomerAssignsIdAndKeepsData() {
        CustomerDTO saved = customerService.createCustomer(newCustomer("ACC-100", 500.0));

        assertNotNull(saved.getId());
        assertEquals("ACC-100", saved.getAccountNumber());
        assertEquals("Ana", saved.getFirstName());
        assertEquals("Perez", saved.getLastName());
        assertEquals(500.0, saved.getBalance());
    }

    @Test
    void getCustomerByIdReturnsTheCustomer() {
        CustomerDTO saved = customerService.createCustomer(newCustomer("ACC-101", 100.0));

        CustomerDTO found = customerService.getCustomerById(saved.getId());

        assertEquals("ACC-101", found.getAccountNumber());
    }

    @Test
    void getCustomerByIdThrowsWhenNotFound() {
        assertThrows(RuntimeException.class, () -> customerService.getCustomerById(999999L));
    }

    @Test
    void getAllCustomersReturnsCreatedCustomers() {
        customerService.createCustomer(newCustomer("ACC-102", 10.0));
        customerService.createCustomer(newCustomer("ACC-103", 20.0));

        List<CustomerDTO> all = customerService.getAllCustomer();

        assertTrue(all.size() >= 2);
    }
}
