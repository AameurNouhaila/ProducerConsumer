package com.projectkafka.services;

import com.projectkafka.entities.Client;
import com.projectkafka.repositories.ClientRepository;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ClientService {
    private final ClientRepository clientRepository;
    private final ConversionService conversionService;

    public ClientService(ClientRepository clientRepository, ConversionService conversionService) {
        this.clientRepository = clientRepository;
        this.conversionService = conversionService;
    }

    public Page<Client> chercher(String search, PageRequest pageRequest) {
        return clientRepository.chercher(search, pageRequest);
    }

    public Page<Client> chercherParDate(LocalDateTime dateDebut, LocalDateTime dateFin, PageRequest pageRequest) {
        return clientRepository.chercherParDate(dateDebut, dateFin, pageRequest);
    }

    public Page<Client> listAll(){
        return (Page<Client>) clientRepository.findAll();
    }

}
