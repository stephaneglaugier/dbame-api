package com.saugier.dbame.registrar.service.impl;

import com.google.gson.Gson;
import com.saugier.dbame.core.model.BallotRequest;
import com.saugier.dbame.core.model.BallotResponse;
import com.saugier.dbame.core.service.impl.CryptoServiceImpl;
import com.saugier.dbame.registrar.exception.AlreadyRegisteredException;
import com.saugier.dbame.registrar.exception.IdNotFoundException;
import com.saugier.dbame.registrar.exception.IncorrectDetailsException;
import com.saugier.dbame.registrar.model.entity.Ballot;
import com.saugier.dbame.core.model.entity.Person;
import com.saugier.dbame.core.model.entity.Roll;
import com.saugier.dbame.registrar.model.entity.SignedBallot;
import com.saugier.dbame.core.repository.IPersonDAO;
import com.saugier.dbame.core.repository.IRollDAO;
import com.saugier.dbame.registrar.repository.ISignedBallotDAO;
import com.saugier.dbame.registrar.service.IRegistrarService;
import com.sun.org.slf4j.internal.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
    private IRollDAO rollDAO;

    @Autowired
    private IPersonDAO personDAO;

    @Autowired
    private ISignedBallotDAO ballotDAO;

    @Autowired
    private CryptoServiceImpl cryptoService;

    @Autowired
    private Gson gson;

    @Override
    public String handleRegisterToVote(String body) throws Exception {

        Person person = gson.fromJson(body, Person.class);

        try{
            checkRecords(person);
        } catch (AlreadyRegisteredException e){
            e.getRoll().setId(null);
            return gson.toJson(e.getRoll());
        }

        Roll out = registerVoter(person);
        return gson.toJson(out);
    }

    private String checkRecords(Person person) throws Exception {

        Optional<Person> record = personDAO.findById(person.getId());
        if (!record.isPresent())
            throw new IdNotFoundException("ERROR: your ID Number was not found in our database", person.getId());
        if (!record.get().equals(person))
            throw new IncorrectDetailsException("ERROR: your details do not match our records", person);
        if (record.get().getRoll() != null)
            throw new AlreadyRegisteredException("ERROR: you have already registered to vote", record.get().getRoll());
        return null;
    }

    public Roll registerVoter(Person person) {

        person.setRoll(cryptoService.sign(person.getRoll()));
        personDAO.save(person);

        person.getRoll().setId(null);
        return person.getRoll();
    }

    @Override
    public String handleGenerateBallots(){
        if (ballotDAO.count() > 0) {
            return "Ballots have already been generated";
        }
        if (rollDAO.count() < 1){
            return "No voters have registered yet";
        }
        generateBallots();
        return "Ballot generation successful";
    }

    public void generateBallots() {
        long startTime = System.nanoTime();

        long n = rollDAO.count();
        List<Long> permutation = generatePermutation(n);
        List<SignedBallot> ballots = new ArrayList<>();
        for (int i=0;i<n;i++){
            Ballot b = new Ballot();
            b.setTimestamp(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
            b.setRandint(ThreadLocalRandom.current().nextInt(0, Integer.MAX_VALUE));
            SignedBallot sb = cryptoService.sign(b);
            sb.setPermutation(permutation.get(i));
            ballots.add(sb);
        }
        ballotDAO.saveAll(ballots);

        long endTime = System.nanoTime();
        String execTime = String.format("Ballots generated and signed in %sms",((endTime - startTime)/1000000));
        log.warn(execTime);
    }

    public List<Long> generatePermutation(long n){
        List<Long> out = new ArrayList<>();
        for (Long i = new Long(1);i<=n;i++){
            out.add(i);
        }
        Collections.shuffle(out);
        return out;
    }

    @Override
    public String handleRequestBallot(String body) {
        BallotRequest ballotRequest = gson.fromJson(body, BallotRequest.class);

        Optional<SignedBallot> signedBallot = ballotDAO.findByPermutation(ballotRequest.getPermutation());

        if (signedBallot.isPresent()){
            try {
                BallotResponse ballotResponse = cryptoService.encryptBallot(signedBallot.get(), ballotRequest);
                return gson.toJson(ballotResponse);
            } catch (Exception e) {
                e.printStackTrace();
                return gson.toJson(e);
            }
        } else {
            throw new NoSuchElementException("Ballot does not exist");
        }
    }
}
