package com.vocalink.stream.processor.service;

import com.vocalink.stream.processor.model.Transaction;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

public class VocalinkServiceImpl implements VocalinkService {

    public CompletionStage<Optional<String>> processTransaction(Transaction tx) {
        if (tx.getMessage().length() > 30) {
            throw new IllegalArgumentException("transaction message too long, 30 characters are allowed");
        }
        if (tx.getMessage().contains("vocalink")) {
            return completedFuture(Optional.empty());
        }
        return completedFuture(
                Optional.of(String.format("transaction message='%s' does not contain %s",
                        tx.getMessage().replace("\n", ""),
                        "vocalink")));
    }

}
