package com.saugier.dbame.registrar.repository.mysql.repository;

import com.saugier.dbame.registrar.model.entity.mysql.RollRE;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRollDAO extends CrudRepository<RollRE, Long> {

    public Optional<RollRE> findByY(String y);
}
