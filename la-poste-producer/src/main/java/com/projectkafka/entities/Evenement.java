package com.projectkafka.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Evenement {
    private Long id;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateEvent;
    private Boolean extract;
    private List<Pli> plis;
}
