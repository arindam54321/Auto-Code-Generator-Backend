package com.ari.auto.code.generator.service.common.all.key;

import com.ari.auto.code.generator.dto.EntityDto;
import com.ari.auto.code.generator.dto.ProjectDto;
import com.ari.auto.code.generator.service.AutoCodeGeneratorService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.zip.ZipOutputStream;

@Service
public class GenerateCommonIdFileService {

    public void generateFiles(ZipOutputStream zipOutputStream, String rootPathForOther, ProjectDto projectDto) throws IOException {
        for (EntityDto entity : projectDto.getEntities()) {
            this.generateEntityId(zipOutputStream, rootPathForOther, projectDto, entity);
        }
    }

    private void generateEntityId(ZipOutputStream zipOutputStream, String rootPathForOther, ProjectDto project, EntityDto entity) throws IOException {
        String className = entity.getEntityName() + "Id";
        String filepath = rootPathForOther + "key/" + className + ".java";
        String content = """
                package %1$s.common.key;
                
                public class %2$sId {
                    public static String generate() {
                         return String.valueOf(System.currentTimeMillis());
                     }
                }
                """.formatted(project.combineGroupAndArtifactId(), entity.getEntityName());

        AutoCodeGeneratorService.addFileInZip(zipOutputStream, filepath, content);
    }
}
