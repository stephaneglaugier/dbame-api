package com.saugier.dbame.registrar.repository;

import com.saugier.dbame.registrar.model.entity.Person;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPersonDAO extends CrudRepository<Person, Long> {
}
