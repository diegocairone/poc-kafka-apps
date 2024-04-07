package com.cairone.poc.event.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventPayload {

    private EventSource source;
    private String op;
    private HashMap<String,Object> before;
    private HashMap<String,Object> after;

}
