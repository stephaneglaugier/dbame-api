package com.saugier.dbame.registrar.repository;

import com.saugier.dbame.registrar.model.entity.Key;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IKeyDAO extends CrudRepository<Key, Long> {
}
