package com.projectkafka.processor;

import com.projectkafka.entities.Client;
import com.projectkafka.entities.Evenement;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

public class ExtractProcessor implements ItemProcessor<Map<String, Object>, Map<String, Object>>  {

    private JdbcTemplate jdbcTemplate;

    public ExtractProcessor(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Map<String, Object> process(Map<String, Object> item) throws Exception {
        item.put("extract", true); // Mettre à jour la valeur de l'attribut 'extract' à true

        // Mettre à jour la valeur extract dans la base de données
        String updateQuery = "UPDATE evenement SET extract = true WHERE id = ?";
        jdbcTemplate.update(updateQuery, item.get("evenement_id"));

        return item;
    }
}
