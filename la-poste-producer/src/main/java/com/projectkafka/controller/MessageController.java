package com.projectkafka.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.projectkafka.entities.Client;
import com.projectkafka.producer.ClientProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;


@RestController
@Slf4j
public class MessageController {

    @Autowired
    private ClientProducer clientProducer;
    @PostMapping("/v1/laposte")
    public ResponseEntity<Client> postClient(@RequestBody Client client) throws JsonProcessingException, ExecutionException, InterruptedException, TimeoutException {

        log.info("before sendClient");
        //clientProducer.sendClient(client);
        clientProducer.sendClient_Approach2(client);
        //log.info("SendResult is {} ", sendResult.toString());
        log.info("after sendClient");
        return ResponseEntity.status(HttpStatus.CREATED).body(client);
    }
}
