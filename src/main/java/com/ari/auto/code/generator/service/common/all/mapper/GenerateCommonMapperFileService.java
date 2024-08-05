package com.ari.auto.code.generator.service.common.all.mapper;

import com.ari.auto.code.generator.dto.EntityDto;
import com.ari.auto.code.generator.dto.FieldDto;
import com.ari.auto.code.generator.dto.ProjectDto;
import com.ari.auto.code.generator.service.AutoCodeGeneratorService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.zip.ZipOutputStream;

@Service
public class GenerateCommonMapperFileService {
    public void generateFiles(ZipOutputStream zipOutputStream, String rootPathForOther, ProjectDto projectDto) throws IOException {
        String __package = "package " + projectDto.combineGroupAndArtifactId() + ".common.mapper;";

        for (EntityDto entity : projectDto.getEntities()) {
            this.generateEntityMapper(zipOutputStream, rootPathForOther, __package, projectDto, entity);
        }
    }

    private void generateEntityMapper(ZipOutputStream zipOutputStream, String rootPathForOther, String __package, ProjectDto projectDto, EntityDto entity) throws IOException {
        String className = entity.getEntityName() + "Mapper";
        String filepath = rootPathForOther + "mapper/" + className + ".java";


        String content = """
                %1$s
                                
                import %2$s.common.dto.%3$sDto;
                import %2$s.common.model.%3$s;
                                
                public class %3$sMapper {
                %4$s
                %5$s
                %6$s}
                """.formatted(__package,
                projectDto.combineGroupAndArtifactId(),
                entity.getEntityName(),
                createToEntityMethod(entity),
                createToDtoMethod(entity),
                createToSecuredDtoMethod(entity));

        AutoCodeGeneratorService.addFileInZip(zipOutputStream, filepath, content);
    }

    String createToEntityMethod(EntityDto entity) {
        StringBuilder otherFields = new StringBuilder();
        String variableName = entity.lowerFirstLetter() + "Dto";

        for (FieldDto field : entity.getFields()) {
            otherFields.append("""
                                .%1$s(%2$s.%3$s())
                    """.formatted(field.getFieldName(), variableName, field.getGetterMethodName()));
        }

        String content = """
                    public static %1$s toEntity(%1$sDto %3$s) {
                        return %1$s.builder()
                            .%2$sId(%3$s.get%1$sId())
                %4$s            .build();
                    }
                """.formatted(entity.getEntityName(), entity.lowerFirstLetter(), variableName, otherFields);

        return content;
    }

    String createToDtoMethod(EntityDto entity) {
        StringBuilder otherFields = new StringBuilder();
        String variableName = entity.lowerFirstLetter();

        for (FieldDto field : entity.getFields()) {
            otherFields.append("""
                                .%1$s(%2$s.%3$s())
                    """.formatted(field.getFieldName(), variableName, field.getGetterMethodName()));
        }

        String content = """
                    public static %1$sDto toDto(%1$s %3$s) {
                        return %1$sDto.builder()
                            .%2$sId(%3$s.get%1$sId())
                %4$s            .build();
                    }
                """.formatted(entity.getEntityName(), entity.lowerFirstLetter(), variableName, otherFields);

        return content;
    }

    String createToSecuredDtoMethod(EntityDto entity) {
        StringBuilder otherFields = new StringBuilder();
        String variableName = entity.lowerFirstLetter();

        for (FieldDto field : entity.getFields()) {
            if (!field.isSensitive()) {
                continue;
            }

            otherFields.append("""
                            %1$sDto.%2$s(null);
                    """.formatted(variableName, field.getSetterMethodName()));
        }

        String content = """
                    public static %1$sDto toSecuredDto(%1$s %3$s) {
                        %1$sDto %2$sDto = %1$sMapper.toDto(%3$s);
                %4$s        return %2$sDto;
                    }
                """.formatted(entity.getEntityName(), entity.lowerFirstLetter(), variableName, otherFields);

        return content;
    }
}
