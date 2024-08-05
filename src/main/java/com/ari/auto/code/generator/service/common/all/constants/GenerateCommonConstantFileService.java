package com.ari.auto.code.generator.service.common.all.constants;

import com.ari.auto.code.generator.dto.EntityDto;
import com.ari.auto.code.generator.dto.ProjectDto;
import com.ari.auto.code.generator.service.AutoCodeGeneratorService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.zip.ZipOutputStream;

@Service
public class GenerateCommonConstantFileService {
    public void generateFiles(ZipOutputStream zipOutputStream, String rootPathForOther, ProjectDto projectDto) throws IOException {
        String __package = projectDto.combineGroupAndArtifactId() + ".common.constants";

        for (EntityDto entity : projectDto.getEntities()) {
            this.generateEntityConstant(zipOutputStream, rootPathForOther, __package, entity);
        }
    }

    private void generateEntityConstant(ZipOutputStream zipOutputStream, String rootPathForOther, String __package, EntityDto entity) throws IOException {
        String className = entity.getEntityName() + "Constants";
        String filepath = rootPathForOther + "constants/" + className + ".java";
        String content = """
                package %1$s;
                                
                public class %2$s {
                    public static final String PATH = "api/v1/%3$ss";
                    public static final String TAG = "%4$s";
                    public static final String DESCRIPTION = "List of %4$s APIs";
                }
                """.formatted(__package, className, entity.toHyphenDelimited(), entity.getEntityName());

        AutoCodeGeneratorService.addFileInZip(zipOutputStream, filepath, content);
    }
}
