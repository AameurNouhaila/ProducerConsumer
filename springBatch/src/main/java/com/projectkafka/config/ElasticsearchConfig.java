package com.projectkafka.config;

import lombok.Value;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticsearchConfig {
    @Bean(destroyMethod = "close")
    public RestHighLevelClient elasticsearchClient() {
        String host = "localhost"; // Remplacez par l'adresse IP ou le nom d'hôte de votre cluster Elasticsearch
        int port = 9200; // Remplacez par le port de votre cluster Elasticsearch

        return new RestHighLevelClient(
                RestClient.builder(new HttpHost(host, port, "http"))
        );
    }


}
