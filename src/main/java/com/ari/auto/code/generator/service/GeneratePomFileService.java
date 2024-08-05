package com.ari.auto.code.generator.service;

import com.ari.auto.code.generator.dto.ProjectDto;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.zip.ZipOutputStream;

@Service
public class GeneratePomFileService {
    public void generateFiles(ZipOutputStream zipOutputStream, ProjectDto projectDto) throws IOException {
        String filepath = "pom.xml";
        String content = """
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
                	<modelVersion>4.0.0</modelVersion>
                	<parent>
                		<groupId>org.springframework.boot</groupId>
                		<artifactId>spring-boot-starter-parent</artifactId>
                		<version>3.3.1</version>
                		<relativePath/>
                	</parent>
                                
                	<groupId>%1$s</groupId>
                	<artifactId>%2$s-all</artifactId>
                	<version>${revision}</version>
                	<name>%3$s-All</name>
                	<packaging>pom</packaging>
                                
                	<properties>
                		<revision>0.0.1-SNAPSHOT</revision>
                		<java.version>17</java.version>
                		<mvn.version>${java.version}</mvn.version>
                		<project.build.sourceEncoding.value>UTF-8</project.build.sourceEncoding.value>
                	</properties>
                                
                	<modules>
                		<module>%2$s-common-all</module>
                		<module>%2$s-common-custom-all</module>
                		<module>%2$s-sync-all</module>
                		<module>%2$s-sync-custom-all</module>
                		<module>%2$s-utils-all</module>
                	</modules>
                                
                	<dependencies>
                		<dependency>
                			<groupId>org.springframework.boot</groupId>
                			<artifactId>spring-boot-starter-web</artifactId>
                		</dependency>
                		<dependency>
                			<groupId>org.springframework.boot</groupId>
                			<artifactId>spring-boot-starter-validation</artifactId>
                		</dependency>
                		<dependency>
                			<groupId>org.springframework.boot</groupId>
                			<artifactId>spring-boot-starter-data-mongodb</artifactId>
                		</dependency>
                		<dependency>
                			<groupId>org.projectlombok</groupId>
                			<artifactId>lombok</artifactId>
                			<optional>true</optional>
                		</dependency>
                		<dependency>
                			<groupId>org.springframework.boot</groupId>
                			<artifactId>spring-boot-starter-test</artifactId>
                			<scope>test</scope>
                		</dependency>
                	</dependencies>
                </project>
                """.formatted(projectDto.getGroupId(),
                projectDto.artifactToHyphenDelimitedLowerCase(),
                projectDto.artifactToHyphenDelimitedCamelCase());
        
        AutoCodeGeneratorService.addFileInZip(zipOutputStream, filepath, content);
    }
}
