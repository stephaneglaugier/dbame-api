package com.saugier.dbame.moderator.repository;

import com.saugier.dbame.moderator.model.entity.ModeratorRelayME;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IEncryptedBallotDAO extends CrudRepository<ModeratorRelayME, Long> {
}
