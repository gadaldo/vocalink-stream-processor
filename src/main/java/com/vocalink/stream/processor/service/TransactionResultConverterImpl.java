package com.vocalink.stream.processor.service;

import com.vocalink.stream.processor.model.TransactionResult;

import java.util.Optional;

public class TransactionResultConverterImpl implements TransactionResultConverter {

    @Override
    public TransactionResult toTransactionResult(Optional<String> opt) {
        return opt
                .map(msg -> TransactionResult.builder()
                        .errorMsg(msg)
                        .status("ERROR")
                        .build())
                .orElse(TransactionResult.builder()
                        .status("SUCCESS")
                        .build());
    }

    @Override
    public TransactionResult toTransactionResult(Throwable exc) {
        return TransactionResult.builder()
                .errorMsg(exc.getMessage())
                .status("FAILED")
                .build();
    }
}
