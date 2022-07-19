package com.saugier.dbame.registrar.repository;

import com.saugier.dbame.registrar.model.entity.BallotRequest;
import org.springframework.data.repository.CrudRepository;

public interface IBallotRequestDAO extends CrudRepository<BallotRequest, Long> {
}
