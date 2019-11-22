package com.vocalink.stream.processor.config;

import akka.util.ByteString;
import com.vocalink.stream.processor.flow.VocalinkStreamProcessor;
import com.vocalink.stream.processor.flow.VocalinkStreamProcessorImpl;
import com.vocalink.stream.processor.model.Transaction;
import com.vocalink.stream.processor.service.TransactionResultConverter;
import com.vocalink.stream.processor.service.TransactionResultConverterImpl;
import com.vocalink.stream.processor.service.VocalinkService;
import com.vocalink.stream.processor.service.VocalinkServiceImpl;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.nio.charset.StandardCharsets;
import java.util.function.Function;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AppConfigurator {

    private static AppConfigurator instance = new AppConfigurator();

    public static AppConfigurator getInstance() {
        return instance;
    }

    public Function<ByteString, Transaction> transactionTranslator() {
        return byteString -> Transaction.builder()
                .message(byteString.decodeString(StandardCharsets.UTF_8))
                .build();
    }

    public VocalinkService vocalinkService() {
        return new VocalinkServiceImpl();
    }

    public TransactionResultConverter transactionResultConverter() {
        return new TransactionResultConverterImpl();
    }

    public VocalinkStreamProcessor vocalinkFlow(Function<ByteString, Transaction> transactionTranslator,
                                                VocalinkService vocalinkService,
                                                TransactionResultConverter transactionResultConverter) {
        return new VocalinkStreamProcessorImpl(transactionTranslator, vocalinkService, transactionResultConverter);
    }
}
