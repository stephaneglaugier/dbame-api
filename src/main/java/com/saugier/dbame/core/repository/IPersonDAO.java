package com.saugier.dbame.core.repository;

import com.saugier.dbame.core.model.entity.Person;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPersonDAO extends CrudRepository<Person, Long> {
}
