package com.cairone.poc.core.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(setterPrefix = "with")
public class MessageModel {

    private String message;
}
