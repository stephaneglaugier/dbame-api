package com.saugier.dbame.core.repository;

import com.saugier.dbame.core.model.entity.Roll;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRollDAO extends CrudRepository<Roll, Long> {

    public Optional<Roll> findByY(String y);
}
