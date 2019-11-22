package com.vocalink.stream.processor;

import akka.actor.ActorSystem;
import com.vocalink.stream.processor.config.AppConfigurator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StreamProcessorMain {

    public static void main(String[] args) {
        log.info("Starting vocalink stream processor");
        final var configurator = AppConfigurator.getInstance();

        final var system = ActorSystem.create("VocalinkStreamProcessor");

        configurator
                .vocalinkFlow(configurator.transactionTranslator(),
                        configurator.vocalinkService(),
                        configurator.transactionResultConverter())
                .run(system, System.in);
    }

}
