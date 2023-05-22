package com.projectkafka.service;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.projectkafka.entity.*;
import com.projectkafka.jpa.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class ClientService {


    @Autowired
    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public void processClient(ConsumerRecord<Integer, String> consumerRecord) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Client client = objectMapper.readValue(consumerRecord.value(), Client.class);
            saveClient(client);
            log.info("Successfully persisted the client: {}", client);
        } catch (IOException e) {
            log.error("Failed to process client data: {}", e.getMessage());
        }
    }

    private void saveClient(Client client) {
        for (Evenement evenement : client.getEvenements()) {
            evenement.setClient(client);
            for (Pli pli : evenement.getPlis()) {
                pli.setEvenement(evenement);
                if (pli.getOffre() != null) {
                    pli.getOffre().setPartenaire(pli.getOffre().getPartenaire());
                }
            }
        }
        clientRepository.save(client);
    }
}
