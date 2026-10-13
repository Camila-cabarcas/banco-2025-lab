package com.ejemplo.banco;

import com.ejemplo.banco.DTO.CustomerDTO;
import com.ejemplo.banco.DTO.TransactionDTO;
import com.ejemplo.banco.DTO.TransferRequestDTO;
import com.ejemplo.banco.entity.Customer;
import com.ejemplo.banco.entity.Transaction;
import com.ejemplo.banco.mapper.CustomerMapper;
import com.ejemplo.banco.mapper.TransactionMapper;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias (sin Spring) de DTOs, entidades y mappers.
 */
class ModelAndMapperTest {

    @Test
    void transferRequestDtoKeepsValues() {
        TransferRequestDTO dto = new TransferRequestDTO();
        dto.setSenderAccountNumber("A-1");
        dto.setReceiverAccountNumber("B-2");
        dto.setAmount(50.0);

        assertEquals("A-1", dto.getSenderAccountNumber());
        assertEquals("B-2", dto.getReceiverAccountNumber());
        assertEquals(50.0, dto.getAmount());
    }

    @Test
    void transactionDtoAllArgsConstructorKeepsValues() {
        LocalDateTime now = LocalDateTime.now();
        TransactionDTO dto = new TransactionDTO(7L, "A-1", "B-2", 20.0, now);

        assertEquals(7L, dto.getId());
        assertEquals("A-1", dto.getSenderAccountNumber());
        assertEquals("B-2", dto.getReceiverAccountNumber());
        assertEquals(20.0, dto.getAmount());
        assertEquals(now, dto.getTimestamp());
    }

    @Test
    void customerConstructorAndSettersKeepValues() {
        Customer customer = new Customer(1L, "ACC-1", "Ana", "Perez", 100.0);
        assertEquals(1L, customer.getId());
        assertEquals("ACC-1", customer.getAccountNumber());
        assertEquals("Ana", customer.getFirstName());
        assertEquals("Perez", customer.getLastName());
        assertEquals(100.0, customer.getBalance());

        customer.setId(2L);
        customer.setAccountNumber("ACC-2");
        customer.setFirstName("Luis");
        customer.setLastName("Gomez");
        customer.setBalance(200.0);
        assertEquals(2L, customer.getId());
        assertEquals("ACC-2", customer.getAccountNumber());
        assertEquals("Luis", customer.getFirstName());
        assertEquals("Gomez", customer.getLastName());
        assertEquals(200.0, customer.getBalance());
    }

    @Test
    void customerMapperConvertsBothWays() {
        CustomerMapper mapper = CustomerMapper.INSTANCE;

        Customer entity = new Customer(3L, "ACC-3", "Ana", "Perez", 300.0);
        CustomerDTO dto = mapper.toDTO(entity);
        assertEquals(3L, dto.getId());
        assertEquals("ACC-3", dto.getAccountNumber());
        assertEquals("Ana", dto.getFirstName());
        assertEquals("Perez", dto.getLastName());
        assertEquals(300.0, dto.getBalance());

        Customer back = mapper.toEntity(dto);
        assertEquals("ACC-3", back.getAccountNumber());
        assertEquals(300.0, back.getBalance());
    }

    @Test
    void customerMapperReturnsNullForNullInput() {
        assertNull(CustomerMapper.INSTANCE.toDTO(null));
        assertNull(CustomerMapper.INSTANCE.toEntity(null));
    }

    @Test
    void transactionMapperConvertsEntity() {
        Transaction transaction = new Transaction();
        transaction.setId(9L);
        transaction.setSenderAccountNumber("A-1");
        transaction.setReceiverAccountNumber("B-2");
        transaction.setAmount(15.0);
        transaction.setTimestamp(LocalDateTime.now());

        TransactionDTO dto = TransactionMapper.INSTANCE.toDTO(transaction);

        assertEquals(9L, dto.getId());
        assertEquals("A-1", dto.getSenderAccountNumber());
        assertEquals("B-2", dto.getReceiverAccountNumber());
        assertEquals(15.0, dto.getAmount());
        assertNotNull(dto.getTimestamp());
        assertNull(TransactionMapper.INSTANCE.toDTO(null));
    }
}
