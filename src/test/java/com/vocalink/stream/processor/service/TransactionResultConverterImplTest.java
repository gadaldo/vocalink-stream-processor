package com.vocalink.stream.processor.service;

import com.vocalink.stream.processor.model.TransactionResult;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TransactionResultConverterImplTest {

    private TransactionResultConverterImpl testObj = new TransactionResultConverterImpl();

    @Test
    void shouldReturnTransactionResultWithErrorMessageAndFailedStatus() {
        assertEquals(TransactionResult.builder()
                .status("ERROR")
                .errorMsg("some error msg")
                .build(), testObj.toTransactionResult(Optional.of("some error msg")));
    }

    @Test
    void shouldReturnTransactionResultWithSuccessStatusAndEmptyErrorMessage() {
        assertEquals(TransactionResult.builder()
                .status("SUCCESS")
                .build(), testObj.toTransactionResult(Optional.empty()));
    }

    @Test
    void shouldReturnTransactionResultWithErrorStatusAndErrorMessage() {
        assertEquals(TransactionResult.builder()
                .status("FAILED")
                .errorMsg("some exception")
                .build(), testObj.toTransactionResult(new RuntimeException("some exception")));
    }

}
