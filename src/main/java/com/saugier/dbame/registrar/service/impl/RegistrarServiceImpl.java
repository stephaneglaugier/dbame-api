package com.saugier.dbame.registrar.service.impl;

import com.google.gson.Gson;
import com.saugier.dbame.core.model.base.Ballot;
import com.saugier.dbame.core.model.base.EncryptedBallot;
import com.saugier.dbame.core.model.base.MaskedRequest;
import com.saugier.dbame.core.model.base.Person;
import com.saugier.dbame.core.model.web.*;
import com.saugier.dbame.core.service.IBaseObjectMapper;
import com.saugier.dbame.core.service.ICryptoService;
import com.saugier.dbame.core.service.IElectionService;
import com.saugier.dbame.registrar.exception.AlreadyRegisteredException;
import com.saugier.dbame.registrar.exception.IdNotFoundException;
import com.saugier.dbame.registrar.exception.IncorrectDetailsException;
import com.saugier.dbame.registrar.exception.NotRegisteredException;
import com.saugier.dbame.registrar.model.entity.h2.BallotRE;
import com.saugier.dbame.registrar.model.entity.mysql.PersonRE;
import com.saugier.dbame.registrar.repository.mysql.IPersonDAO;
import com.saugier.dbame.registrar.repository.mysql.IRollDAO;
import com.saugier.dbame.registrar.repository.h2.ISignedBallotDAO;
import com.saugier.dbame.registrar.service.IRegistrarObjectMapper;
import com.saugier.dbame.registrar.service.IRegistrarService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class RegistrarServiceImpl implements IRegistrarService {

    @Autowired
    private Logger log;

    @Autowired
    private IElectionService electionService;

    @Autowired
    private IRollDAO rollDAO;

    @Autowired
    private IPersonDAO personDAO;

    @Autowired
    private ISignedBallotDAO ballotDAO;

    @Autowired
    private ICryptoService cryptoService;

    @Autowired
    private Gson gson;

    @Autowired
    private IRegistrarObjectMapper registrarObjectMapper;

    @Autowired
    private IBaseObjectMapper baseObjectMapper;

    
    public RegistrationResponse handleRegisterToVote(RegistrationRequest registrationRequest) throws Exception {

        Person person = baseObjectMapper.map(registrationRequest);

        try{
            checkRecords(person);
        } catch (AlreadyRegisteredException e){
            return baseObjectMapper.map(e.getPerson());
        }

        if (!electionService.getElectionState().equalsIgnoreCase("registration")){
            throw new NotRegisteredException("ERROR: you have not registered to vote", person.getId());
        }

        RegistrationResponse out = registerVoter(person);
        return out;
    }

    private String checkRecords(Person person) throws Exception {

        PersonRE personRE = registrarObjectMapper.map(person);

        Optional<PersonRE> record = personDAO.findById(personRE.getId());
        if (!record.isPresent())
            throw new IdNotFoundException("ERROR: your ID Number was not found in our database", personRE.getId());
        if (!record.get().equals(personRE))
            throw new IncorrectDetailsException("ERROR: your details do not match our records", personRE);
        if (record.get().getRoll() != null)
            throw new AlreadyRegisteredException("ERROR: you have already registered to vote", registrarObjectMapper.map(record.get()));
        return null;
    }

    public RegistrationResponse registerVoter(Person person) throws Exception {

        person.setRoll(cryptoService.sign(person.getRoll()));

        personDAO.save(registrarObjectMapper.map(person));

        return baseObjectMapper.map(person);
    }
    
    public String handleGenerateBallots() throws Exception {
        if (ballotDAO.count() == rollDAO.getMaxId()) {
            return "Ballots have already been generated";
        }
        if (rollDAO.count() < 1){
            return "No voters have registered yet";
        }
        generateBallots();
        return "BallotRE generation successful";
    }

    public void generateBallots() throws Exception {
        long startTime = System.nanoTime();

        long n = rollDAO.getMaxId();
        List<Long> permutation = generatePermutation(n);
        List<BallotRE> ballots = new ArrayList<>();
        for (int i=0;i<n;i++){
            Ballot b = new Ballot();
            b.setTimestamp(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
            b.setRandint(ThreadLocalRandom.current().nextInt(0, Integer.MAX_VALUE));
            Ballot sb = cryptoService.sign(b);
            BallotRE sbRE = registrarObjectMapper.map(sb, permutation.get(i));
            sbRE.setId(i);
            ballots.add(sbRE);
        }

        ballotDAO.saveAll(ballots);

        long endTime = System.nanoTime();
        String execTime = String.format("Ballots generated and signed in %sms",((endTime - startTime)/1000000));
        log.warn(execTime);
    }

    private List<Long> generatePermutation(long n){
        List<Long> out = new ArrayList<>();
        for (Long i = new Long(1);i<=n;i++){
            out.add(i);
        }
        Collections.shuffle(out);
        return out;
    }

    
    public BallotRelayResponse handleRequestBallot(BallotRelayRequest ballotRelayRequest) throws Exception {

        MaskedRequest maskedRequest = baseObjectMapper.map(ballotRelayRequest);

        Optional<BallotRE> record = ballotDAO.findByPermutation(maskedRequest.getPermutation());
        if (record.isPresent()){
            Ballot ballot = registrarObjectMapper.map(record.get());
            EncryptedBallot encryptedBallot = cryptoService.encryptBallot(
                    ballot,
                    maskedRequest.getMaskedData(),
                    maskedRequest.getPermutation()
            );
            BallotRelayResponse out = baseObjectMapper.map(encryptedBallot);
            return out;

        } else {
            throw new NoSuchElementException("Ballot does not exist");
        }
    }

    
    public ElectionParams handleElectionParams() throws Exception {
        return electionService.asElectionParams();
    }
}
