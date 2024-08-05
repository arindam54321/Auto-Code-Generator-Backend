package com.ari.auto.code.generator.service.common.all.model;

import com.ari.auto.code.generator.dto.EntityDto;
import com.ari.auto.code.generator.dto.FieldDto;
import com.ari.auto.code.generator.dto.ProjectDto;
import com.ari.auto.code.generator.service.AutoCodeGeneratorService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.zip.ZipOutputStream;

@Service
public class GenerateCommonModelFileService {
    public void generateFiles(ZipOutputStream zipOutputStream, String rootPathForOther, ProjectDto projectDto) throws IOException {
        String __package_and_includes = "package " + projectDto.combineGroupAndArtifactId() + ".common.model;" + """
                                
                import lombok.AllArgsConstructor;
                import lombok.Builder;
                import lombok.Data;
                import lombok.NoArgsConstructor;
                import org.springframework.data.annotation.Id;
                import org.springframework.data.mongodb.core.mapping.Document;
                import org.springframework.data.mongodb.core.mapping.Field;
                
                """;

        for (EntityDto entity : projectDto.getEntities()) {
            this.generateEntityMapper(zipOutputStream, rootPathForOther, __package_and_includes, entity);
        }
    }

    private void generateEntityMapper(ZipOutputStream zipOutputStream, String rootPathForOther, String __package_and_includes, EntityDto entity) throws IOException {
        String className = entity.getEntityName();
        String filepath = rootPathForOther + "model/" + className + ".java";

        if (entity.hasListFields()) {
            __package_and_includes += "import java.util.List;\n\n";
        }

        StringBuilder bodyContent = new StringBuilder("""
                    @Id
                    @Field(name = "%1$sid")
                    String %2$sId;
                """.formatted(entity.getEntityName().toLowerCase(), entity.lowerFirstLetter()));

        for (FieldDto field : entity.getFields()) {
            bodyContent.append("""
                        
                        @Field(name = "%1$s")
                        %2$s %3$s;
                    """.formatted(field.getFieldName().toLowerCase(), field.getDataType().getType(), field.getFieldName()));
        }

        bodyContent.append("""
                    
                    @Field(name = "softdeleted")
                    boolean softDeleted;
                """);

        String content = """
                %1$s
                
                @Data
                @Builder
                @NoArgsConstructor
                @AllArgsConstructor
                @Document(collection = "%2$ss")
                public class %2$s {
                %3$s}
                """.formatted(__package_and_includes, className, bodyContent);

        AutoCodeGeneratorService.addFileInZip(zipOutputStream, filepath, content);
    }
}
