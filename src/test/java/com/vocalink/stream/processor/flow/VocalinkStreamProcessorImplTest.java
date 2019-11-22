package com.vocalink.stream.processor.flow;

import akka.actor.ActorSystem;
import akka.testkit.javadsl.TestKit;
import akka.util.ByteString;
import com.vocalink.stream.processor.model.Transaction;
import com.vocalink.stream.processor.service.TransactionResultConverterImpl;
import com.vocalink.stream.processor.service.VocalinkService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import scala.concurrent.duration.Duration;

import java.io.ByteArrayInputStream;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static java.util.concurrent.CompletableFuture.completedFuture;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class VocalinkStreamProcessorImplTest {

    @Mock
    private Function<ByteString, Transaction> transactionTranslator;

    @Mock
    private VocalinkService vocalinkService;

    @Spy
    private TransactionResultConverterImpl transactionResultConverter;

    @InjectMocks
    private VocalinkStreamProcessorImpl testObj;

    private ActorSystem system;

    @BeforeEach
    void setup() {
        system = ActorSystem.create("StreamTestKitDocTest");
    }

    @Test
    void shouldOutputSuccessfulTransactionResult() {
        final var message = "a successful message vocalink";
        final var inputStream = new ByteArrayInputStream(message.getBytes());

        final var transaction = Transaction.builder().message(message).build();
        when(transactionTranslator.apply(any())).thenReturn(transaction);
        when(vocalinkService.processTransaction(transaction)).thenReturn(completedFuture(Optional.empty()));

        testObj.run(system, inputStream);

        TestKit.shutdownActorSystem(system, Duration.create(500L, MILLISECONDS), true);

        verify(transactionTranslator, times(1)).apply(any());
        verify(vocalinkService, times(1)).processTransaction(transaction);
        verify(transactionResultConverter, times(1)).toTransactionResult(Optional.empty());
    }

    @Test
    void shouldOutputFailedTransactionResult() {
        final var message = "a failing message";
        final var inputStream = new ByteArrayInputStream(message.getBytes());

        final var transaction = Transaction.builder().message(message).build();
        final var expectedErrorMsg = String.format("transaction message='%s' does not contain %s", message, "vocalink");

        when(transactionTranslator.apply(any())).thenReturn(transaction);
        when(vocalinkService.processTransaction(transaction)).thenReturn(completedFuture(Optional.of(expectedErrorMsg)));

        testObj.run(system, inputStream);

        TestKit.shutdownActorSystem(system, Duration.create(500L, MILLISECONDS), true);

        verify(transactionTranslator, times(1)).apply(any());
        verify(vocalinkService, times(1)).processTransaction(transaction);
        verify(transactionResultConverter, times(1)).toTransactionResult(Optional.of(expectedErrorMsg));
    }

    @Test
    void shouldHandleErrorWhenOccur() {
        final var message = "an error message with more than 30 characters";
        final var inputStream = new ByteArrayInputStream(message.getBytes());

        final var transaction = Transaction.builder().message(message).build();
        final var expectedErrorMsg = "transaction message too long, 30 characters are allowed";
        final var expectedException = new IllegalArgumentException(expectedErrorMsg);

        when(transactionTranslator.apply(any())).thenReturn(transaction);
        when(vocalinkService.processTransaction(transaction)).thenThrow(expectedException);

        testObj.run(system, inputStream);

        TestKit.shutdownActorSystem(system, Duration.create(500L, MILLISECONDS), true);

        verify(transactionTranslator, times(1)).apply(any());
        verify(vocalinkService, times(1)).processTransaction(transaction);
        verify(transactionResultConverter, times(1)).toTransactionResult(expectedException);
    }

}
