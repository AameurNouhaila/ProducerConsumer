package com.projectkafka.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Offre {
    private Long id;
    private int codeOffre;
    private int dtyOffre;
    private String description;
    private Partenaire partenaire;
}
