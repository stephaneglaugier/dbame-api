package com.saugier.dbame.moderator.repository;

import com.saugier.dbame.moderator.model.entity.PermutationME;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPermutationDAO extends CrudRepository<PermutationME, Long> {
}
