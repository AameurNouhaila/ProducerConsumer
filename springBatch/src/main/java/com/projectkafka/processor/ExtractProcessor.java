package com.projectkafka.processor;

import com.projectkafka.entities.Client;
import com.projectkafka.entities.Evenement;
import org.springframework.batch.item.ItemProcessor;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class ExtractProcessor implements ItemProcessor<Evenement,Evenement> {

    @Override
    public Evenement process(Evenement evenement) {
        evenement.setExtract(true);

        return evenement;
    }
}
