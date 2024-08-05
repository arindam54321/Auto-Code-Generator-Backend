package com.ari.auto.code.generator.service.utils.all;

import com.ari.auto.code.generator.dto.ProjectDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.zip.ZipOutputStream;

@Service
public class GenerateUtilsModuleFilesService {

    private final GenerateUtilsPomFileService generateUtilsPomFileService;
    private final GenerateUtilsOtherFileService generateUtilsOtherFileService;

    @Autowired
    public GenerateUtilsModuleFilesService(
            GenerateUtilsPomFileService generateUtilsPomFileService,
            GenerateUtilsOtherFileService generateUtilsOtherFileService
    ) {
        this.generateUtilsPomFileService = generateUtilsPomFileService;
        this.generateUtilsOtherFileService = generateUtilsOtherFileService;
    }

    public void generateFiles(ZipOutputStream zipOutputStream, ProjectDto projectDto) throws IOException {
        String rootPathForPom = projectDto.artifactToHyphenDelimitedLowerCase() + "-utils-all/";
        String rootPathForOther = rootPathForPom
                + "src/main/java/"
                + projectDto.combineGroupAndArtifactId().replace(".", "/")
                + "/utils/";

        generateUtilsPomFileService.generateFiles(zipOutputStream, rootPathForPom, projectDto);
        generateUtilsOtherFileService.generateFiles(zipOutputStream, rootPathForOther, projectDto);
    }
}
