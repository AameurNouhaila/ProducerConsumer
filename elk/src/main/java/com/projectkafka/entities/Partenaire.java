package com.projectkafka.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
public class Partenaire {
    @Id
    private Long id;
    private String nom;
    private String description;
    @JsonBackReference
    private List<Offre> offres;
}
