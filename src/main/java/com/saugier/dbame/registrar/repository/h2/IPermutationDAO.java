package com.saugier.dbame.registrar.repository.h2;

import com.saugier.dbame.registrar.model.entity.h2.PermutationME;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IPermutationDAO extends CrudRepository<PermutationME, Long> {

    Optional<PermutationME> findByFrom(long from);
}
