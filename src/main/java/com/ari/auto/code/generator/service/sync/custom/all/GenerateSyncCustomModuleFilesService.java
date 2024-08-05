package com.ari.auto.code.generator.service.sync.custom.all;

import com.ari.auto.code.generator.dto.ProjectDto;
import com.ari.auto.code.generator.service.AutoCodeGeneratorService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.zip.ZipOutputStream;

@Service
public class GenerateSyncCustomModuleFilesService {
    public void generateFiles(ZipOutputStream zipOutputStream, ProjectDto projectDto) throws IOException {
        String rootPathForPom = projectDto.artifactToHyphenDelimitedLowerCase() + "-sync-custom-all/";
        String rootPathForOther = rootPathForPom
                + "src/main/java/"
                + projectDto.combineGroupAndArtifactId().replace(".", "/")
                + "/sync/";
        String rootPathForResources = rootPathForPom + "src/main/resources/";
        String DELETE_ME_FILE_NAME = ".DELETE_ME";
        String DELETE_ME_FILE_CONTENT = "THIS FILE IS FOR MAINTAINING FOLDER STRUCTURE ONLY";

        this.generatePomFile(zipOutputStream, rootPathForPom, projectDto);
        this.generateResourcesFiles(zipOutputStream, rootPathForResources, projectDto);
        this.generateApplicationFile(zipOutputStream, rootPathForOther, projectDto);
        AutoCodeGeneratorService.addFileInZip(zipOutputStream, rootPathForOther + "controller/" + DELETE_ME_FILE_NAME, DELETE_ME_FILE_CONTENT);
        AutoCodeGeneratorService.addFileInZip(zipOutputStream, rootPathForOther + "repository/" + DELETE_ME_FILE_NAME, DELETE_ME_FILE_CONTENT);
        AutoCodeGeneratorService.addFileInZip(zipOutputStream, rootPathForOther + "service/" + DELETE_ME_FILE_NAME, DELETE_ME_FILE_CONTENT);
    }

    private void generateResourcesFiles(ZipOutputStream zipOutputStream, String rootPathForResources, ProjectDto projectDto) throws IOException {
        String filepath = rootPathForResources + "application.properties";
        String content = "spring.application.name=%1$sSyncCustomAll".formatted(projectDto.artifactToHyphenDelimitedCamelCase().replace("-", ""));
        AutoCodeGeneratorService.addFileInZip(zipOutputStream, filepath, content);
    }

    private void generateApplicationFile(ZipOutputStream zipOutputStream, String rootPathForOther, ProjectDto projectDto) throws IOException {
        String filepath = rootPathForOther + "Application.java";
        String content = """
                package %1$s.sync;
                                
                import org.springframework.boot.SpringApplication;
                import org.springframework.boot.autoconfigure.SpringBootApplication;
                                
                @SpringBootApplication
                public class Application {
                    public static void main(String[] args) {
                        SpringApplication.run(Application.class, args);
                    }
                }
                """.formatted(projectDto.combineGroupAndArtifactId());

        AutoCodeGeneratorService.addFileInZip(zipOutputStream, filepath, content);
    }

    private void generatePomFile(ZipOutputStream zipOutputStream, String rootPathForPom, ProjectDto projectDto) throws IOException {
        String filepath = rootPathForPom + "pom.xml";
        String content = """
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0"
                       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                       xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                    <modelVersion>4.0.0</modelVersion>
                    
                    <artifactId>%2$s-sync-custom-all</artifactId>
                    <name>%3$s-Sync-Custom-All</name>
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
                            <artifactId>%2$s-common-custom-all</artifactId>
                            <version>${revision}</version>
                        </dependency>
                        <dependency>
                            <groupId>%1$s</groupId>
                            <artifactId>%2$s-sync-all</artifactId>
                            <version>${revision}</version>
                        </dependency>
                        <dependency>
                            <groupId>org.springdoc</groupId>
                            <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
                            <version>2.0.2</version>
                        </dependency>
                    </dependencies>
                    
                    <build>
                        <plugins>
                            <plugin>
                                <groupId>org.springframework.boot</groupId>
                                <artifactId>spring-boot-maven-plugin</artifactId>
                                <configuration>
                                    <excludes>
                                        <exclude>
                                            <groupId>org.projectlombok</groupId>
                                            <artifactId>lombok</artifactId>
                                        </exclude>
                                    </excludes>
                                </configuration>
                            </plugin>
                        </plugins>
                    </build>
                </project>
                """.formatted(projectDto.getGroupId(),
                projectDto.artifactToHyphenDelimitedLowerCase(),
                projectDto.artifactToHyphenDelimitedCamelCase());

        AutoCodeGeneratorService.addFileInZip(zipOutputStream, filepath, content);
    }
}
