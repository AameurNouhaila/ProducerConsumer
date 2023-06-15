package com.projectkafka.services;

import com.projectkafka.entities.Client;
import com.projectkafka.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

@Service
public class ClientService {
    private final ClientRepository clientRepository;
    private final ConversionService conversionService;

    public ClientService(ClientRepository clientRepository, ConversionService conversionService) {
        this.clientRepository = clientRepository;
        this.conversionService = conversionService;
    }

    public Page<Client> getAllClients() {
        return clientRepository.findAll(PageRequest.of(0, 27));
    }


}
