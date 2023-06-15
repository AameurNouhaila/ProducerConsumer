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
        String host = "localhost";
        int port = 9200;

        return new RestHighLevelClient(
                RestClient.builder(new HttpHost(host, port, "http"))
        );
    }


}
