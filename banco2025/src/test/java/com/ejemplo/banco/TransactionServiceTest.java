package com.ejemplo.banco;

import com.ejemplo.banco.DTO.CustomerDTO;
import com.ejemplo.banco.DTO.TransactionDTO;
import com.ejemplo.banco.service.CustomerService;
import com.ejemplo.banco.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class TransactionServiceTest {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private CustomerService customerService;

    private Long senderId;
    private Long receiverId;

    @BeforeEach
    void setUp() {
        senderId = customerService.createCustomer(
                new CustomerDTO(null, "Ana", "Perez", "SEND-1", 1000.0)).getId();
        receiverId = customerService.createCustomer(
                new CustomerDTO(null, "Luis", "Gomez", "RECV-1", 200.0)).getId();
    }

    private TransactionDTO transfer(String from, String to, double amount) {
        TransactionDTO dto = new TransactionDTO();
        dto.setSenderAccountNumber(from);
        dto.setReceiverAccountNumber(to);
        dto.setAmount(amount);
        return dto;
    }

    @Test
    void transferMovesMoneyBetweenAccounts() {
        TransactionDTO result = transactionService.transferMoney(transfer("SEND-1", "RECV-1", 300.0));

        assertNotNull(result.getId());
        assertNotNull(result.getTimestamp());
        assertEquals(300.0, result.getAmount());
        assertEquals(700.0, customerService.getCustomerById(senderId).getBalance());
        assertEquals(500.0, customerService.getCustomerById(receiverId).getBalance());
    }

    @Test
    void transferFailsWhenBalanceIsNotEnough() {
        TransactionDTO request = transfer("SEND-1", "RECV-1", 5000.0);

        assertThrows(IllegalArgumentException.class, () -> transactionService.transferMoney(request));
        assertEquals(1000.0, customerService.getCustomerById(senderId).getBalance());
    }

    @Test
    void transferFailsWhenAccountIsNull() {
        TransactionDTO request = transfer(null, "RECV-1", 10.0);

        assertThrows(IllegalArgumentException.class, () -> transactionService.transferMoney(request));
    }

    @Test
    void transferFailsWhenAccountDoesNotExist() {
        TransactionDTO request = transfer("NO-EXISTE", "RECV-1", 10.0);

        assertThrows(IllegalArgumentException.class, () -> transactionService.transferMoney(request));
    }

    @Test
    void transactionsCanBeListedFoundUpdatedAndDeleted() {
        Long id = transactionService.transferMoney(transfer("SEND-1", "RECV-1", 50.0)).getId();

        List<TransactionDTO> all = transactionService.getAllTransactions();
        assertFalse(all.isEmpty());

        assertEquals(1, transactionService.getTransactionsForAccount("SEND-1").size());

        TransactionDTO found = transactionService.getTransactionById(id);
        assertEquals("SEND-1", found.getSenderAccountNumber());

        TransactionDTO updated = transactionService.updateTransaction(id, transfer("SEND-1", "RECV-1", 75.0));
        assertEquals(75.0, updated.getAmount());

        transactionService.deleteTransaction(id);
        assertThrows(IllegalArgumentException.class, () -> transactionService.getTransactionById(id));
    }
}
