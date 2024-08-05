package com.ari.auto.code.generator.service.common.all.dto;

import com.ari.auto.code.generator.dto.EntityDto;
import com.ari.auto.code.generator.dto.FieldDto;
import com.ari.auto.code.generator.dto.ProjectDto;
import com.ari.auto.code.generator.service.AutoCodeGeneratorService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.zip.ZipOutputStream;

@Service
public class GenerateCommonDtoFileService {
    public void generateFiles(ZipOutputStream zipOutputStream, String rootPathForOther, ProjectDto projectDto) throws IOException {
        String __package_and_includes = "package " + projectDto.combineGroupAndArtifactId() + ".common.dto;" + """
                                
                import com.fasterxml.jackson.annotation.JsonInclude;
                import jakarta.validation.constraints.Null;
                import lombok.AllArgsConstructor;
                import lombok.Builder;
                import lombok.Data;
                import lombok.NoArgsConstructor;
                
                """;

        for (EntityDto entity : projectDto.getEntities()) {
            this.generateEntityDto(zipOutputStream, rootPathForOther, __package_and_includes, entity);
        }
    }

    private void generateEntityDto(ZipOutputStream zipOutputStream, String rootPathForOther, String __package_and_includes, EntityDto entity) throws IOException {
        String className = entity.getEntityName() + "Dto";
        String filepath = rootPathForOther + "dto/" + className + ".java";

        if (entity.hasListFields()) {
            __package_and_includes += "import java.util.List;\n\n";
        }

        __package_and_includes += "import static com.fasterxml.jackson.annotation.JsonInclude.Include;";

        StringBuilder bodyContent = new StringBuilder("""
                    @Null(message = "ID should not be provided, it will be auto-generated")
                    String %1$sId;
                """.formatted(entity.lowerFirstLetter()));

        for (FieldDto field : entity.getFields()) {
            bodyContent.append("""
                        
                        %1$s %2$s;
                    """.formatted(field.getDataType().getType(), field.getFieldName()));
        }

        String content = """
                %1$s
                
                @Data
                @Builder
                @NoArgsConstructor
                @AllArgsConstructor
                @JsonInclude(Include.NON_NULL)
                public class %2$s {
                %3$s}
                """.formatted(__package_and_includes, className, bodyContent);

        AutoCodeGeneratorService.addFileInZip(zipOutputStream, filepath, content);
    }
}
