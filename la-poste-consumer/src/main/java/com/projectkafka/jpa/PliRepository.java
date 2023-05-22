package com.projectkafka.jpa;


import com.projectkafka.entity.Pli;
import org.springframework.data.repository.CrudRepository;

public interface PliRepository extends CrudRepository<Pli, Long> {
}
