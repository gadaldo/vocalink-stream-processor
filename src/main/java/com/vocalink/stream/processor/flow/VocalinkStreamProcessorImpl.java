package com.vocalink.stream.processor.flow;

import akka.actor.ActorSystem;
import akka.stream.ActorAttributes;
import akka.stream.Supervision;
import akka.stream.javadsl.StreamConverters;
import akka.util.ByteString;
import com.vocalink.stream.processor.model.Transaction;
import com.vocalink.stream.processor.service.TransactionResultConverter;
import com.vocalink.stream.processor.service.VocalinkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

@Slf4j
@RequiredArgsConstructor
public class VocalinkStreamProcessorImpl implements VocalinkStreamProcessor {

    private final Function<ByteString, Transaction> transactionTranslator;

    private final VocalinkService vocalinkService;

    private final TransactionResultConverter transactionResultConverter;

    @Override
    public void run(ActorSystem actorSystem, InputStream inputStream) {
        log.info("run");

        StreamConverters.fromInputStream(() -> inputStream)
                .map(transactionTranslator::apply)
                .map(vocalinkService::processTransaction)
                .withAttributes(ActorAttributes.withSupervisionStrategy(exc -> {
                    log.info("processed transaction={}", transactionResultConverter.toTransactionResult(exc));
                    return Supervision.restart();
                }))
                .map(CompletionStage::toCompletableFuture)
                .map(CompletableFuture::get)
                .map(transactionResultConverter::toTransactionResult)
                .runForeach(result -> log.info("processed transaction={}", result), actorSystem);
    }

}
