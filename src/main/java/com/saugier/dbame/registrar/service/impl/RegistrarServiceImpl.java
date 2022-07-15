package com.saugier.dbame.registrar.service.impl;

import com.google.gson.Gson;
import com.saugier.dbame.core.service.impl.CryptoServiceImpl;
import com.saugier.dbame.registrar.model.entity.Person;
import com.saugier.dbame.registrar.model.entity.Roll;
import com.saugier.dbame.registrar.repository.IKeyDAO;
import com.saugier.dbame.registrar.repository.IPersonDAO;
import com.saugier.dbame.registrar.repository.IRollDAO;
import com.saugier.dbame.registrar.service.IRegistrarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RegistrarServiceImpl implements IRegistrarService {

    @Autowired
    private IRollDAO rollDAO;

    @Autowired
    private IPersonDAO personDAO;

    @Autowired
    private IKeyDAO keyDAO;

    @Autowired
    private CryptoServiceImpl cryptoService;

    @Autowired
    private Gson gson;

    @Override
    public String handleRegisterToVote(String body) throws Exception {

        Person person = gson.fromJson(body, Person.class);

        checkRecords(person);

        Roll out = registerVoter(person);

        return gson.toJson(out);
    }

    private String checkRecords(Person person) throws Exception {

        Optional<Person> record = personDAO.findById(person.getId());
        if (!record.isPresent()) throw new Exception("ERROR: your ID Number was not found in our database");
        if (!record.get().equals(person)) throw new Exception("ERROR: your details do not match our records");
        return null;
    }

    public Roll registerVoter(Person person) {

        Roll roll = new Roll();
        roll.setKey(person.getKey());
        roll = cryptoService.EGSignRoll(roll);

//        keyDAO.save(person.getKey());
        rollDAO.save(roll);
        personDAO.save(person);

        return roll;
    }
}
