package com.cairone.poc.core.model;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.UUID;

@Value
@Builder(setterPrefix = "with")
public class FooRecord {

    private UUID id;
    private String name;
    private LocalDateTime createdAt;
}
