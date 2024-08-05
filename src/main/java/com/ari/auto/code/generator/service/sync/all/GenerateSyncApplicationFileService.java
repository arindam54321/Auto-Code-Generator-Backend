package com.ari.auto.code.generator.service.sync.all;

import com.ari.auto.code.generator.dto.EntityDto;
import com.ari.auto.code.generator.dto.ProjectDto;
import com.ari.auto.code.generator.service.AutoCodeGeneratorService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.zip.ZipOutputStream;

@Service
public class GenerateSyncApplicationFileService {
    public void generateFiles(ZipOutputStream zipOutputStream, String rootPathForOther, ProjectDto projectDto) throws IOException {
        String __package = "package " + projectDto.combineGroupAndArtifactId() + ".sync;";
        String filepath = rootPathForOther + "Application.java";
        String content = """
                %s
                                
                import org.springframework.boot.SpringApplication;
                import org.springframework.boot.autoconfigure.SpringBootApplication;
                                
                @SpringBootApplication
                public class Application {
                    public static void main(String[] args) {
                        SpringApplication.run(Application.class, args);
                    }
                }
                """.formatted(__package);

        AutoCodeGeneratorService.addFileInZip(zipOutputStream, filepath, content);
    }
}
