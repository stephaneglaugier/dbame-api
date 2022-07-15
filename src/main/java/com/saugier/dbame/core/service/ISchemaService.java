package com.saugier.dbame.core.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Set;

@Service
public interface ISchemaService {

    static void validate(String json, String schemaPath) throws Exception {

        InputStream schemaAsStream = new FileInputStream(schemaPath);
        JsonSchema schema = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7).getSchema(schemaAsStream);

        ObjectMapper om = new ObjectMapper();
        JsonNode jsonNode = om.readTree(json);

        Set<ValidationMessage> errors = schema.validate(jsonNode);

        if (errors.size() > 0)
            throw new Exception("Please fix your json! " + errors);

    }
}
