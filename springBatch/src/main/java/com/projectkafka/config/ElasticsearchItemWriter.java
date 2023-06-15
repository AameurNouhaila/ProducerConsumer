package com.projectkafka.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.batch.item.ItemWriter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class ElasticsearchItemWriter implements ItemWriter<Map<String, Object>> {

    private RestHighLevelClient client;
    private String index;
    private ObjectMapper objectMapper;
    private SimpleDateFormat dateFormat;

    public ElasticsearchItemWriter(RestHighLevelClient client, String index) {
        this.client = client;
        this.index = index;

        // Initialisez le dateFormat avec le format souhaité
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        this.dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        // Initialiser l'objectMapper avec le module Jackson JSR310
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public void write(List<? extends Map<String, Object>> items) throws Exception {
        for (Map<String, Object> item : items) {
            // Convertir la Map en JSON
            String json = convertMapToJson(item);

            // Mettre à jour la date en chaîne formatée
            convertDateToString(item, "date-naissance");

            // Créer une requête d'indexation
            IndexRequest request = new IndexRequest(index)
                    .source(json, XContentType.JSON);

            try {
                // Indexation du document dans Elasticsearch
                IndexResponse response = client.index(request, RequestOptions.DEFAULT);

            } catch (IOException e) {
                // Gérer les erreurs d'indexation
                e.printStackTrace();
            }
        }
    }

    private void convertDateToString(Map<String, Object> map, String fieldName) {
        if (map.containsKey(fieldName)) {
            Object value = map.get(fieldName);
            if (value instanceof Date) {
                Date date = (Date) value;
                String formattedDate = dateFormat.format(date);
                map.put(fieldName, formattedDate);
            }
        }
    }

    private String convertMapToJson(Map<String, Object> map) {
        try {
            // Convertir la Map en JSON en utilisant l'objectMapper configuré avec le module Jackson JSR310
            return objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            // Gérer les erreurs de sérialisation JSON
            e.printStackTrace();
            return null;
        }
    }
}

