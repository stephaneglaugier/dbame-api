package com.saugier.dbame.registrar.repository.h2;

import com.saugier.dbame.registrar.model.entity.h2.PermutationME;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IPermutationDAO extends CrudRepository<PermutationME, Long> {
}
