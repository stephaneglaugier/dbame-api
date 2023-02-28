package com.saugier.dbame.registrar.repository.h2;

import com.saugier.dbame.registrar.model.entity.h2.BallotRE;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ISignedBallotDAO extends CrudRepository<BallotRE, Long> {
    Optional<BallotRE> findByPermutation(long permutation);
}
