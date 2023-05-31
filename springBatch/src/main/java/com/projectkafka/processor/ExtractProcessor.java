package com.projectkafka.processor;

import com.projectkafka.entities.Client;
import com.projectkafka.entities.Evenement;
import org.springframework.batch.item.ItemProcessor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

public class ExtractProcessor implements ItemProcessor<Map<String, Object>, Map<String, Object>>  {

    @Override
    public Map<String, Object> process(Map<String, Object> item) throws Exception {
        item.put("extract", true); // Mettre à jour la valeur de l'attribut 'extract' à true
        return item;
    }
}
