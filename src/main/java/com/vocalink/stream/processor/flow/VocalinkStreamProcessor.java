package com.vocalink.stream.processor.flow;

import akka.actor.ActorSystem;

import java.io.InputStream;

public interface VocalinkStreamProcessor {

    void run(ActorSystem actorSystem, InputStream inputStream);
}
