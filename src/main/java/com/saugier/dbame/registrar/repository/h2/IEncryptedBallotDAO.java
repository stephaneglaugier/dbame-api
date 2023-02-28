package com.saugier.dbame.registrar.repository;

import com.saugier.dbame.moderator.model.entity.ModeratorRelayME;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IEncryptedBallotDAO extends CrudRepository<ModeratorRelayME, Long> {
}
