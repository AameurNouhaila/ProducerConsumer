package com.projectkafka.controller;

import com.projectkafka.entities.Client;
import com.projectkafka.services.ClientService;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/la-poste")
public class ClientController {
    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/clients")
    public ResponseEntity<Page<Client>> getAllClients() {
        Page<Client> clients = clientService.getAllClients();
        return ResponseEntity.ok(clients);
    }
}
