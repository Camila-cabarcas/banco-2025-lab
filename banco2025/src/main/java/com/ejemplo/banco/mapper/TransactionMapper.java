package com.ejemplo.banco.mapper;

import com.ejemplo.banco.DTO.TransactionDTO;
import com.ejemplo.banco.entity.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TransactionMapper {
    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);

    TransactionDTO toDTO(Transaction transaction);
}