package com.projectkafka.jpa;

import com.projectkafka.entity.Evenement;
import org.springframework.data.repository.CrudRepository;

public interface EvenementRepository extends CrudRepository<Evenement, Long> {
}
