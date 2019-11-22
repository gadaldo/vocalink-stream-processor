package com.vocalink.stream.processor.service;

import com.vocalink.stream.processor.model.Transaction;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

public interface VocalinkService {

    CompletionStage<Optional<String>> processTransaction(Transaction tx);

}
