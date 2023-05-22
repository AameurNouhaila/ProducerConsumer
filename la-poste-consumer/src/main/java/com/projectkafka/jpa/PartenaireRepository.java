package com.projectkafka.jpa;

import com.projectkafka.entity.Partenaire;
import org.springframework.data.repository.CrudRepository;

public interface PartenaireRepository extends CrudRepository<Partenaire, Long> {
}
