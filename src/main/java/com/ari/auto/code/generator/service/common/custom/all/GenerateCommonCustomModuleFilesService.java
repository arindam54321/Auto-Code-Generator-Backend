package com.ari.auto.code.generator.service.common.custom.all;

import com.ari.auto.code.generator.dto.ProjectDto;
import com.ari.auto.code.generator.service.AutoCodeGeneratorService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.zip.ZipOutputStream;

@Service
public class GenerateCommonCustomModuleFilesService {
    public void generateFiles(ZipOutputStream zipOutputStream, ProjectDto projectDto) throws IOException {
        String rootPathForPom = projectDto.artifactToHyphenDelimitedLowerCase() + "-common-custom-all/";
        String rootPathForOther = rootPathForPom
                + "src/main/java/"
                + projectDto.combineGroupAndArtifactId().replace(".", "/")
                + "/common/";
        String DELETE_ME_FILE_NAME = ".DELETE_ME";
        String DELETE_ME_FILE_CONTENT = "THIS FILE IS FOR MAINTAINING FOLDER STRUCTURE ONLY";

        this.generatePomFile(zipOutputStream, rootPathForPom, projectDto);
        AutoCodeGeneratorService.addFileInZip(zipOutputStream, rootPathForOther + "constants/" + DELETE_ME_FILE_NAME, DELETE_ME_FILE_CONTENT);
        AutoCodeGeneratorService.addFileInZip(zipOutputStream, rootPathForOther + "dto/" + DELETE_ME_FILE_NAME, DELETE_ME_FILE_CONTENT);
        AutoCodeGeneratorService.addFileInZip(zipOutputStream, rootPathForOther + "exceptions/" + DELETE_ME_FILE_NAME, DELETE_ME_FILE_CONTENT);
        AutoCodeGeneratorService.addFileInZip(zipOutputStream, rootPathForOther + "key/" + DELETE_ME_FILE_NAME, DELETE_ME_FILE_CONTENT);
        AutoCodeGeneratorService.addFileInZip(zipOutputStream, rootPathForOther + "mapper/" + DELETE_ME_FILE_NAME, DELETE_ME_FILE_CONTENT);
        AutoCodeGeneratorService.addFileInZip(zipOutputStream, rootPathForOther + "model/" + DELETE_ME_FILE_NAME, DELETE_ME_FILE_CONTENT);
    }

    private void generatePomFile(ZipOutputStream zipOutputStream, String rootPathForPom, ProjectDto projectDto) throws IOException {
        String filepath = rootPathForPom + "pom.xml";
        String content = """
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0"
                         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                    <modelVersion>4.0.0</modelVersion>
                                
                    <artifactId>%2$s-common-custom-all</artifactId>
                    <name>%3$s-Common-Custom-All</name>
                    <version>${revision}</version>
                                
                    <parent>
                        <groupId>%1$s</groupId>
                        <artifactId>%2$s-all</artifactId>
                        <version>${revision}</version>
                    </parent>
                                
                    <properties>
                        <maven.compiler.source>${mvn.version}</maven.compiler.source>
                        <maven.compiler.target>${mvn.version}</maven.compiler.target>
                        <project.build.sourceEncoding>${project.build.sourceEncoding.value}</project.build.sourceEncoding>
                    </properties>
                                
                    <dependencies>
                        <dependency>
                            <groupId>%1$s</groupId>
                            <artifactId>%2$s-common-all</artifactId>
                            <version>${revision}</version>
                        </dependency>
                    </dependencies>
                
                </project>
                """.formatted(projectDto.getGroupId(),
                projectDto.artifactToHyphenDelimitedLowerCase(),
                projectDto.artifactToHyphenDelimitedCamelCase());

        AutoCodeGeneratorService.addFileInZip(zipOutputStream, filepath, content);
    }
}
