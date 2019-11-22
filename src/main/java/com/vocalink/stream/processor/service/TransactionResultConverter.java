package com.vocalink.stream.processor.service;

import com.vocalink.stream.processor.model.TransactionResult;

import java.util.Optional;

public interface TransactionResultConverter {

    TransactionResult toTransactionResult(Optional<String> opt);

    TransactionResult toTransactionResult(Throwable exc);
}
