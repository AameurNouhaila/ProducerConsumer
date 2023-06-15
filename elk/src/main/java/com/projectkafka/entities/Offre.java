package com.projectkafka.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Offre {
    @Id
    private Long id;
    private int codeOffre;
    private int dtyOffre;
    private String description;
    @JsonBackReference
    private List<Pli> plis;
    @JsonManagedReference
    private Partenaire partenaire;
}
