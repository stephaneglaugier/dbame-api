package com.saugier.dbame.registrar.repository.mysql.repository;

import com.saugier.dbame.registrar.model.entity.mysql.PersonRE;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPersonDAO extends CrudRepository<PersonRE, Long> {
}
