package com.ari.auto.code.generator.service.sync.all.repository;

import com.ari.auto.code.generator.dto.EntityDto;
import com.ari.auto.code.generator.dto.FieldDto;
import com.ari.auto.code.generator.dto.ProjectDto;
import com.ari.auto.code.generator.service.AutoCodeGeneratorService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.zip.ZipOutputStream;

@Service
public class GenerateSyncRepositoryFilesService {

    public void generateFiles(ZipOutputStream zipOutputStream, String rootPathForOther, ProjectDto projectDto) throws IOException {
        String __package = "package " + projectDto.combineGroupAndArtifactId() + ".sync.repository;";

        for (EntityDto entity : projectDto.getEntities()) {
            this.generateSyncRepository(zipOutputStream, rootPathForOther, __package, projectDto, entity);
        }
    }

    private void generateSyncRepository(ZipOutputStream zipOutputStream, String rootPathForOther, String __package, ProjectDto projectDto, EntityDto entity) throws IOException {
        String className = entity.getEntityName() + "Repository";
        String filepath = rootPathForOther + "repository/" + className + ".java";

        String content = """
                %1$s
                
                import %2$s.common.model.%3$s;
                import org.springframework.data.mongodb.repository.MongoRepository;
                                
                public interface %3$sRepository extends MongoRepository<%3$s, String> {
                %4$s}
                """.formatted(__package,
                projectDto.combineGroupAndArtifactId(),
                entity.getEntityName(),
                this.addExistByUniqueKeysMethods(entity));

        AutoCodeGeneratorService.addFileInZip(zipOutputStream, filepath, content);
    }

    private String addExistByUniqueKeysMethods(EntityDto entity) {
        StringBuilder content = new StringBuilder();

        for (FieldDto field : entity.getFields()) {
            if (field.isUnique()) {
                content.append("""
                        boolean %1$s(String %2$s, boolean softDeleted);
                    """.formatted(field.getExistByMethodName(), field.getFieldName()));
            }
        }

        return content.toString();
    }
}
