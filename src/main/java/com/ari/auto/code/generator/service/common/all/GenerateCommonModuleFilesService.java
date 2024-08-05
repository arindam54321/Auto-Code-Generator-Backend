package com.ari.auto.code.generator.service.common.all;

import com.ari.auto.code.generator.dto.ProjectDto;
import com.ari.auto.code.generator.service.common.all.constants.GenerateCommonConstantFileService;
import com.ari.auto.code.generator.service.common.all.dto.GenerateCommonDtoFileService;
import com.ari.auto.code.generator.service.common.all.exceptions.GenerateCommonExceptionFileService;
import com.ari.auto.code.generator.service.common.all.key.GenerateCommonIdFileService;
import com.ari.auto.code.generator.service.common.all.mapper.GenerateCommonMapperFileService;
import com.ari.auto.code.generator.service.common.all.model.GenerateCommonModelFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.zip.ZipOutputStream;

@Service
public class GenerateCommonModuleFilesService {

    private final GenerateCommonPomFileService generateCommonPomFileService;
    private final GenerateCommonConstantFileService generateCommonConstantFileService;
    private final GenerateCommonExceptionFileService generateCommonExceptionFileService;
    private final GenerateCommonDtoFileService generateCommonDtoFileService;
    private final GenerateCommonModelFileService generateCommonModelFileService;
    private final GenerateCommonMapperFileService generateCommonMapperFileService;
    private final GenerateCommonIdFileService generateCommonIdFileService;

    @Autowired
    public GenerateCommonModuleFilesService(
        GenerateCommonPomFileService generateCommonPomFileService,
        GenerateCommonConstantFileService generateCommonConstantFileService,
        GenerateCommonExceptionFileService generateCommonExceptionFileService,
        GenerateCommonDtoFileService generateCommonDtoFileService,
        GenerateCommonModelFileService generateCommonModelFileService,
        GenerateCommonMapperFileService generateCommonMapperFileService,
        GenerateCommonIdFileService generateCommonIdFileService
    ) {
        this.generateCommonPomFileService = generateCommonPomFileService;
        this.generateCommonConstantFileService = generateCommonConstantFileService;
        this.generateCommonExceptionFileService = generateCommonExceptionFileService;
        this.generateCommonDtoFileService = generateCommonDtoFileService;
        this.generateCommonModelFileService = generateCommonModelFileService;
        this.generateCommonMapperFileService = generateCommonMapperFileService;
        this.generateCommonIdFileService = generateCommonIdFileService;
    }

    public void generateFiles(ZipOutputStream zipOutputStream, ProjectDto projectDto) throws IOException {
        String rootPathForPom = projectDto.artifactToHyphenDelimitedLowerCase() + "-common-all/";
        String rootPathForOther = rootPathForPom
                + "src/main/java/"
                + projectDto.combineGroupAndArtifactId().replace(".", "/")
                + "/common/";

        this.generateCommonPomFileService.generateFiles(zipOutputStream, rootPathForPom, projectDto);
        this.generateCommonConstantFileService.generateFiles(zipOutputStream, rootPathForOther, projectDto);
        this.generateCommonExceptionFileService.generateFiles(zipOutputStream, rootPathForOther, projectDto);
        this.generateCommonDtoFileService.generateFiles(zipOutputStream, rootPathForOther, projectDto);
        this.generateCommonModelFileService.generateFiles(zipOutputStream, rootPathForOther, projectDto);
        this.generateCommonMapperFileService.generateFiles(zipOutputStream, rootPathForOther, projectDto);
        this.generateCommonIdFileService.generateFiles(zipOutputStream, rootPathForOther, projectDto);
    }
}
