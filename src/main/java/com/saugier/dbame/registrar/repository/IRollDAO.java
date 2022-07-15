package com.saugier.dbame.registrar.repository;

import com.saugier.dbame.registrar.model.entity.Roll;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRollDAO extends CrudRepository<Roll, Long> {
}
