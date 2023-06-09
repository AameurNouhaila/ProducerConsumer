package com.projectkafka.config;

import com.projectkafka.entities.Client;
import com.projectkafka.entities.Evenement;
import com.projectkafka.processor.ExtractProcessor;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersIncrementer;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.PassThroughFieldExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    DataSource dataSource;
    @Autowired
    private RestHighLevelClient elasticsearchClient;
    @Bean
    public JdbcCursorItemReader<Map<String, Object>> reader() {
        JdbcCursorItemReader<Map<String, Object>> reader = new JdbcCursorItemReader<>();
        reader.setDataSource(dataSource);
        reader.setSql("SELECT client.id AS client_id, client.prenom, client.nom, client.cin, client.date_naissance, client.adresse, " +
                "evenement.id AS evenement_id, evenement.date_event, evenement.extract, " +
                "pli.id AS pli_id, pli.poids, pli.longueur, pli.largeur, pli.niveau_recommandation, pli.montant, " +
                "offre.id AS offre_id, offre.code_offre, offre.dty_offre, offre.description, " +
                "partenaire.id AS partenaire_id, partenaire.nom AS partenaire_nom, partenaire.description AS partenaire_description " +
                "FROM client " +
                "LEFT JOIN evenement ON client.id = evenement.client_id " +
                "LEFT JOIN pli ON evenement.id = pli.evenement_id " +
                "LEFT JOIN offre ON pli.offre_id = offre.id " +
                "LEFT JOIN partenaire ON offre.partenaire_id = partenaire.id " +
                "WHERE evenement.extract = false"
                );
        reader.setRowMapper(new ColumnMapRowMapper());
        return reader;
    }

    @Bean
    public ItemWriter<Map<String, Object>> elasticsearchItemWriter() {
        String index = "la-poste";
        return new ElasticsearchItemWriter(elasticsearchClient, index);

    }


    @Bean
    public Step executeStep(ItemReader<Map<String, Object>> reader,
                            ItemWriter<Map<String, Object>> elasticsearchItemWriter) {
        return stepBuilderFactory.get("executeStep")
                .<Map<String, Object>, Map<String, Object>>chunk(10)
                .reader(reader())
                .processor(extractProcessor())
                .writer(elasticsearchItemWriter)
                .build();
    }
    @Bean
    public ItemProcessor<Map<String, Object>, Map<String, Object>> extractProcessor() {
        return new ExtractProcessor(dataSource);
    }

    @Bean
    public Job processJob(Step executeStep) {
        return jobBuilderFactory.get("processJob")
                .incrementer(new RunIdIncrementer())
                .start(executeStep)
                .build();
    }



}
