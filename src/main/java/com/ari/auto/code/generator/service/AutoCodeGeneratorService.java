package com.ari.auto.code.generator.service;

import com.ari.auto.code.generator.dto.ProjectDto;
import com.ari.auto.code.generator.service.common.all.GenerateCommonModuleFilesService;
import com.ari.auto.code.generator.service.common.custom.all.GenerateCommonCustomModuleFilesService;
import com.ari.auto.code.generator.service.sync.all.GenerateSyncModuleFilesService;
import com.ari.auto.code.generator.service.sync.custom.all.GenerateSyncCustomModuleFilesService;
import com.ari.auto.code.generator.service.utils.all.GenerateUtilsModuleFilesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class AutoCodeGeneratorService {

    private final GenerateBasicFilesService generateBasicFilesService;
    private final GeneratePomFileService generatePomFileService;
    private final GenerateUtilsModuleFilesService generateUtilsModuleFilesService;
    private final GenerateCommonModuleFilesService generateCommonModuleFilesService;
    private final GenerateCommonCustomModuleFilesService generateCommonCustomModuleFilesService;
    private final GenerateSyncModuleFilesService generateSyncModuleFilesService;
    private final GenerateSyncCustomModuleFilesService generateSyncCustomModuleFilesService;

    @Autowired
    public AutoCodeGeneratorService(
            GenerateBasicFilesService generateBasicFilesService,
            GeneratePomFileService generatePomFileService,
            GenerateUtilsModuleFilesService generateUtilsModuleFilesService,
            GenerateCommonModuleFilesService generateCommonModuleFilesService,
            GenerateCommonCustomModuleFilesService generateCommonCustomModuleFilesService,
            GenerateSyncModuleFilesService generateSyncModuleFilesService,
            GenerateSyncCustomModuleFilesService generateSyncCustomModuleFilesService
    ) {
        this.generateBasicFilesService = generateBasicFilesService;
        this.generatePomFileService = generatePomFileService;
        this.generateUtilsModuleFilesService = generateUtilsModuleFilesService;
        this.generateCommonModuleFilesService = generateCommonModuleFilesService;
        this.generateCommonCustomModuleFilesService = generateCommonCustomModuleFilesService;
        this.generateSyncModuleFilesService = generateSyncModuleFilesService;
        this.generateSyncCustomModuleFilesService = generateSyncCustomModuleFilesService;
    }

    public byte[] generate(ProjectDto projectDto) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream)) {
            generateBasicFilesService.generateFiles(zipOutputStream);
            generatePomFileService.generateFiles(zipOutputStream, projectDto);
            generateUtilsModuleFilesService.generateFiles(zipOutputStream, projectDto);
            generateCommonModuleFilesService.generateFiles(zipOutputStream, projectDto);
            generateCommonCustomModuleFilesService.generateFiles(zipOutputStream, projectDto);
            generateSyncModuleFilesService.generateFiles(zipOutputStream, projectDto);
            generateSyncCustomModuleFilesService.generateFiles(zipOutputStream, projectDto);
        }
        return byteArrayOutputStream.toByteArray();
    }

    public static void addFileInZip(
            ZipOutputStream zipOutputStream, String filePath, String content
    ) throws IOException {
        ZipEntry zipEntry = new ZipEntry(filePath);
        zipOutputStream.putNextEntry(zipEntry);
        zipOutputStream.write(content.getBytes());
        zipOutputStream.closeEntry();
    }
}
