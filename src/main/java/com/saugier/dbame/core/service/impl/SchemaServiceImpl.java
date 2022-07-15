package com.saugier.dbame.core.service.impl;

import com.google.gson.Gson;
import com.saugier.dbame.core.service.ISchemaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SchemaServiceImpl implements ISchemaService {

    @Autowired
    Gson gson;


}
