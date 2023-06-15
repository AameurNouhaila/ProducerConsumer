package com.projectkafka.repositories;

import com.projectkafka.entities.Client;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ClientRepository extends ElasticsearchRepository<Client, Integer> {
}
