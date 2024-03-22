package com.cairone.poc.rest.ctrl;

import com.cairone.poc.core.service.MessageService;
import com.cairone.poc.rest.request.MessageRequest;
import com.cairone.poc.core.model.MessageModel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageProducerCtrl {

    private final MessageService messageService;

    @PostMapping
    public ResponseEntity<MessageModel> sendMessage(MessageRequest request) {
        MessageModel messageModel = messageService.sendMessage(request.getText(), request.getPartition());
        return ResponseEntity.ok(messageModel);
    }
}
