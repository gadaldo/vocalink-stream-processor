package com.vocalink.stream.processor.model;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
@Getter
@RequiredArgsConstructor
public class Transaction {

    private final String message;
}
