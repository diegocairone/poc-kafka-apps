package com.cairone.poc.rest.request;

import lombok.Data;

@Data
public class MessageRequest {

    private int partition;
    private String text;
}
