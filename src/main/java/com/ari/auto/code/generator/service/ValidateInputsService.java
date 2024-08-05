package com.ari.auto.code.generator.service;

import com.ari.auto.code.generator.dto.EntityDto;
import com.ari.auto.code.generator.dto.FieldDto;
import com.ari.auto.code.generator.dto.ProjectDto;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class ValidateInputsService {

    public String validate(ProjectDto projectDto) throws Exception {
        Set<String> entities = new HashSet<String>();

        for (EntityDto entity : projectDto.getEntities()) {
            String entityName = entity.getEntityName().toLowerCase();

            if (entities.contains(entityName)) {
                throw new Exception("Two or more Entities exist with same name: " + entity.getEntityName());
            }

            entities.add(entityName);

            Set<String> fields = new HashSet<String>();

            for (FieldDto field : entity.getFields()) {
                String fieldName = field.getFieldName().toLowerCase();

                if (fields.contains(fieldName)) {
                    throw new Exception(entity.getEntityName() + ": Two or more Fields exist with same name: " + field.getFieldName());
                }

                fields.add(fieldName);
            }
        }

        return "Success: Input is Valid";
    }
}
