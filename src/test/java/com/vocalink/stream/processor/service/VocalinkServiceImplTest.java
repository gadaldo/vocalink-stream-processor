package com.vocalink.stream.processor.service;

import com.vocalink.stream.processor.model.Transaction;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class VocalinkServiceImplTest {

    private VocalinkServiceImpl testObj = new VocalinkServiceImpl();

    @Test
    void shouldReturnOptionalEmptyWhenTransactionMessageContainsVocalink() throws Exception {
        final var stage = testObj.processTransaction(Transaction.builder()
                .message("some vocalink msg")
                .build());

        assertEquals(Optional.empty(), stage.toCompletableFuture().get());
    }

    @Test
    void shouldThrowIllegalArgumentExcWhenTransactionMessageLongerThen30Characters() {
        final var message = assertThrows(
                IllegalArgumentException.class,
                () -> testObj.processTransaction(Transaction.builder()
                        .message("some vocalink msg longer than 30 characters")
                        .build()))
                .getMessage();

        assertEquals("transaction message too long, 30 characters are allowed", message);
    }

    @Test
    void shouldReturnOptionalWithErrorMessageWhenTransactionMessageDoesNotContainVocalink() throws Exception {
        final var message = testObj.processTransaction(Transaction.builder()
                .message("some transaction message")
                .build());

        final var optionalMessage = Optional.of("transaction message='some transaction message' does not contain vocalink");

        assertEquals(optionalMessage, message.toCompletableFuture().get());
    }
}
