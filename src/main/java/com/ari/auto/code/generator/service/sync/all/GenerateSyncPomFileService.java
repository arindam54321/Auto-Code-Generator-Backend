package com.ari.auto.code.generator.service.sync.all;

import com.ari.auto.code.generator.dto.ProjectDto;
import com.ari.auto.code.generator.service.AutoCodeGeneratorService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.zip.ZipOutputStream;

@Service
public class GenerateSyncPomFileService {
    public void generateFiles(ZipOutputStream zipOutputStream, String rootPathForPom, ProjectDto projectDto) throws IOException {
        String filepath = rootPathForPom + "pom.xml";
        String content = """
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0"
                       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                       xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                    <modelVersion>4.0.0</modelVersion>
                    
                    <artifactId>%2$s-sync-all</artifactId>
                    <name>%3$s-Sync-All</name>
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
                        <dependency>
                            <groupId>%1$s</groupId>
                            <artifactId>%2$s-utils-all</artifactId>
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
