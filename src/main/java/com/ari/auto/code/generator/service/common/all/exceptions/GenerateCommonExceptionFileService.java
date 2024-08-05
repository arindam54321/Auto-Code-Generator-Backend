package com.ari.auto.code.generator.service.common.all.exceptions;

import com.ari.auto.code.generator.dto.EntityDto;
import com.ari.auto.code.generator.dto.ProjectDto;
import com.ari.auto.code.generator.service.AutoCodeGeneratorService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.zip.ZipOutputStream;

@Service
public class GenerateCommonExceptionFileService {
    public void generateFiles(ZipOutputStream zipOutputStream, String rootPathForOther, ProjectDto projectDto) throws IOException {
        String __package = projectDto.combineGroupAndArtifactId() + ".common.exception";

        for (EntityDto entity : projectDto.getEntities()) {
            this.generateEntityException(zipOutputStream, rootPathForOther, __package, entity);
        }
    }

    private void generateEntityException(ZipOutputStream zipOutputStream, String rootPathForOther, String __package, EntityDto entity) throws IOException {
        String className = entity.getEntityName() + "Exception";
        String filepath = rootPathForOther + "exception/" + className + ".java";
        String content = """
                package %1$s;
                
                public class %2$s extends Exception {
                    public %2$s(String message) {
                        super(message);
                    }
                }
                """.formatted(__package, className);

        AutoCodeGeneratorService.addFileInZip(zipOutputStream, filepath, content);
    }
}
