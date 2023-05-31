package com.projectkafka.config;

import com.projectkafka.entities.Client;
import com.projectkafka.entities.Evenement;
import com.projectkafka.processor.ExtractProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersIncrementer;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    DataSource dataSource;
    @Bean
    public JdbcCursorItemReader<Client> reader(){
        JdbcCursorItemReader<Client> readerEvent = new JdbcCursorItemReader<Client>();
        readerEvent.setDataSource(dataSource);
        readerEvent.setSql("select * from client ");
        readerEvent.setRowMapper(new RowMapper<Client>() {
            @Override
            public Client mapRow(ResultSet rs, int rowNum) throws SQLException {

                Client client = new Client();
                client.setId(rs.getInt("id"));
                client.setPrenom(rs.getString("prenom"));
                client.setNom(rs.getString("nom"));
                client.setCin(rs.getString("cin"));
                client.setDateNaissance(rs.getDate("date_naissance"));
                client.setAdresse(rs.getString("adresse"));

                return client;
            }
        });
        return readerEvent;
    }

    @Bean
    public FlatFileItemWriter<Client> writer(){
        FlatFileItemWriter<Client> writerEvent = new FlatFileItemWriter<>();
        writerEvent.setResource(new FileSystemResource("C:/nouhaBatch/nouha.csv"));
        DelimitedLineAggregator<Client> aggregator = new DelimitedLineAggregator<>();
        BeanWrapperFieldExtractor<Client> fieldExtractor = new BeanWrapperFieldExtractor<>();
        fieldExtractor.setNames(new String[] {"id","prenom","nom","cin","dateNaissance","adresse"});
        aggregator.setFieldExtractor(fieldExtractor);
        writerEvent.setLineAggregator(aggregator);
        return writerEvent;
    }

    @Bean
    public JdbcCursorItemReader<Evenement> readerEvent(){
        JdbcCursorItemReader<Evenement> readerEvent = new JdbcCursorItemReader<Evenement>();
        readerEvent.setDataSource(dataSource);
        readerEvent.setSql("select * from evenement ");
        readerEvent.setRowMapper(new RowMapper<Evenement>() {
            @Override
            public Evenement mapRow(ResultSet rs, int rowNum) throws SQLException {

                Evenement evenement = new Evenement();
                evenement.setId(rs.getLong("id"));
                evenement.setDateEvent(rs.getDate("date_event"));
                evenement.setExtract(rs.getBoolean("extract"));

                return evenement;
            }
        });
        return readerEvent;
    }

    @Bean
    public FlatFileItemWriter<Evenement> writerEvent(){
        FlatFileItemWriter<Evenement> writerEvent = new FlatFileItemWriter<>();
        writerEvent.setResource(new FileSystemResource("C:/nouhaBatch/nouha.csv"));
        DelimitedLineAggregator<Evenement> aggregator = new DelimitedLineAggregator<>();
        BeanWrapperFieldExtractor<Evenement> fieldExtractor = new BeanWrapperFieldExtractor<>();
        fieldExtractor.setNames(new String[] {"id","dateEvent","extract"});
        aggregator.setFieldExtractor(fieldExtractor);
        writerEvent.setLineAggregator(aggregator);
        return writerEvent;
    }

    @Bean
    public Step executeStep(){
        return stepBuilderFactory.get("executeStep").<Client, Client>chunk(10)
                .reader(reader())
                .writer(writer())
                .build();
    }

    @Bean
    public Step executeStep2(){
        return stepBuilderFactory.get("executeStep2").
                        <Evenement, Evenement>chunk(10)
                .reader(readerEvent())
                .processor(new ExtractProcessor())
                .writer(writerEvent())
                .build();
    }
    @Bean
    public Job processJob(){
        return jobBuilderFactory.get("processJob").incrementer(new RunIdIncrementer())
                .flow(executeStep())
                .next(executeStep2())
                .end()
                .build();
    }

}
