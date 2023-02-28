package com.saugier.dbame.registrar.repository.h2;

import com.saugier.dbame.registrar.model.entity.h2.ModeratorRelayME;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IEncryptedBallotDAO extends CrudRepository<ModeratorRelayME, Long> {
}
