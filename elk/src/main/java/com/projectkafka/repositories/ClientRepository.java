package com.projectkafka.repositories;

import com.projectkafka.entities.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ClientRepository extends ElasticsearchRepository<Client, String> {

    @Query("{\"bool\": {\"must\": [{\"match\": {\"niveau_recommandation\": \"?0\"}}]}}")
    public Page<Client> chercher(
            @Param("search") String search, Pageable pageable);

    @Query("{\"bool\": {\"must\": [{\"range\": {\"date_event\": {\"gte\": \"?0\", \"lte\": \"?1\"}}}]}}")
    public Page<Client> chercherParDate(
            @Param("dateDebut") LocalDateTime dateDebut, @Param("dateFin") LocalDateTime dateFin, Pageable pageable);


}
