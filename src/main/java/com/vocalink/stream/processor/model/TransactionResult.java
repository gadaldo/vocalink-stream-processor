package com.vocalink.stream.processor.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@Builder
@EqualsAndHashCode
@RequiredArgsConstructor
public class TransactionResult {

    private final String errorMsg;
    private final String status;

}
