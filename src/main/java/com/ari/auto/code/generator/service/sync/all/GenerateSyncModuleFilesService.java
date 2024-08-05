package com.ari.auto.code.generator.service.sync.all;

import com.ari.auto.code.generator.dto.ProjectDto;
import com.ari.auto.code.generator.service.sync.all.contoller.GenerateSyncControllerFilesService;
import com.ari.auto.code.generator.service.sync.all.repository.GenerateSyncRepositoryFilesService;
import com.ari.auto.code.generator.service.sync.all.service.GenerateSyncServiceFilesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.zip.ZipOutputStream;

@Service
public class GenerateSyncModuleFilesService {

    private final GenerateSyncPomFileService generateSyncPomFileService;
    private final GenerateSyncApplicationFileService generateSyncApplicationFileService;
    private final GenerateSyncExceptionHandlerFileService generateSyncExceptionHandlerFileService;
    private final GenerateSyncResourcesFilesService generateSyncResourcesFilesService;
    private final GenerateSyncRepositoryFilesService generateSyncRepositoryFilesService;
    private final GenerateSyncServiceFilesService generateSyncServiceFilesService;
    private final GenerateSyncControllerFilesService generateSyncControllerFilesService;

    @Autowired
    public GenerateSyncModuleFilesService(
            GenerateSyncPomFileService generateSyncPomFileService,
            GenerateSyncApplicationFileService generateSyncApplicationFileService,
            GenerateSyncExceptionHandlerFileService generateSyncExceptionHandlerFileService,
            GenerateSyncResourcesFilesService generateSyncResourcesFilesService,
            GenerateSyncRepositoryFilesService generateSyncRepositoryFilesService,
            GenerateSyncServiceFilesService generateSyncServiceFilesService,
            GenerateSyncControllerFilesService generateSyncControllerFilesService
    ) {
        this.generateSyncPomFileService = generateSyncPomFileService;
        this.generateSyncApplicationFileService = generateSyncApplicationFileService;
        this.generateSyncExceptionHandlerFileService = generateSyncExceptionHandlerFileService;
        this.generateSyncResourcesFilesService = generateSyncResourcesFilesService;
        this.generateSyncRepositoryFilesService = generateSyncRepositoryFilesService;
        this.generateSyncServiceFilesService = generateSyncServiceFilesService;
        this.generateSyncControllerFilesService = generateSyncControllerFilesService;
    }

    public void generateFiles(ZipOutputStream zipOutputStream, ProjectDto projectDto) throws IOException {
        String rootPathForPom = projectDto.artifactToHyphenDelimitedLowerCase() + "-sync-all/";
        String rootPathForOther = rootPathForPom
                + "src/main/java/"
                + projectDto.combineGroupAndArtifactId().replace(".", "/")
                + "/sync/";
        String rootPathForResources = rootPathForPom + "src/main/resources/";

        this.generateSyncPomFileService.generateFiles(zipOutputStream, rootPathForPom, projectDto);
        this.generateSyncResourcesFilesService.generateFiles(zipOutputStream, rootPathForResources, projectDto);
        this.generateSyncApplicationFileService.generateFiles(zipOutputStream, rootPathForOther, projectDto);
        this.generateSyncExceptionHandlerFileService.generateFiles(zipOutputStream, rootPathForOther, projectDto);
        this.generateSyncRepositoryFilesService.generateFiles(zipOutputStream, rootPathForOther, projectDto);
        this.generateSyncServiceFilesService.generateFiles(zipOutputStream, rootPathForOther, projectDto);
        this.generateSyncControllerFilesService.generateFiles(zipOutputStream, rootPathForOther, projectDto);
    }
}
