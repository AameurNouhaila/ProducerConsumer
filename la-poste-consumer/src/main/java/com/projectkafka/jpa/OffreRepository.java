package com.projectkafka.jpa;

import com.projectkafka.entity.Offre;
import org.springframework.data.repository.CrudRepository;

public interface OffreRepository extends CrudRepository<Offre, Long> {
}
