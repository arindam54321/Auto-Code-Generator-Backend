package com.ari.auto.code.generator.service.sync.all;

import com.ari.auto.code.generator.dto.ProjectDto;
import com.ari.auto.code.generator.service.AutoCodeGeneratorService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.zip.ZipOutputStream;

@Service
public class GenerateSyncResourcesFilesService {
    public void generateFiles(ZipOutputStream zipOutputStream, String rootPathForResources, ProjectDto projectDto) throws IOException {
        generatePropertiesFile(zipOutputStream, rootPathForResources, projectDto);
        generateYmlFile(zipOutputStream, rootPathForResources, projectDto);
    }

    void generatePropertiesFile(ZipOutputStream zipOutputStream, String rootPathForResources, ProjectDto projectDto) throws IOException {
        String filepath = rootPathForResources + "application.properties";
        String content = "spring.application.name=%1$sSyncAll".formatted(projectDto.artifactToHyphenDelimitedCamelCase().replace("-", ""));
        AutoCodeGeneratorService.addFileInZip(zipOutputStream, filepath, content);
    }

    void generateYmlFile(ZipOutputStream zipOutputStream, String rootPathForResources, ProjectDto projectDto) throws IOException {
        String filepath = rootPathForResources + "application.yml";
        String content = """
                server:
                  port: %1$s
                spring:
                  data:
                    mongodb:
                      uri: mongodb://localhost:27017/%2$s
                """.formatted((int) (1000 + Math.random() * 10000), projectDto.toHyphenDelimited());
        AutoCodeGeneratorService.addFileInZip(zipOutputStream, filepath, content);
    }
}
