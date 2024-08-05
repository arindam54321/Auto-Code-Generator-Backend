package com.ari.auto.code.generator.service.sync.all.contoller;

import com.ari.auto.code.generator.dto.EntityDto;
import com.ari.auto.code.generator.dto.ProjectDto;
import com.ari.auto.code.generator.service.AutoCodeGeneratorService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.zip.ZipOutputStream;

@Service
public class GenerateSyncControllerFilesService {
    public void generateFiles(ZipOutputStream zipOutputStream, String rootPathForOther, ProjectDto projectDto) throws IOException {
        for (EntityDto entity : projectDto.getEntities()) {
            String className = entity.getEntityName() + "Controller";
            String filepath = rootPathForOther + "controller/" + className + ".java";
            String content = """
                package %1$s.sync.controller;
                                
                import %1$s.common.constants.%2$sConstants;
                import %1$s.common.dto.%2$sDto;
                import %1$s.sync.service.I%2$sService;
                import %1$s.utils.ApiResult;
                import %1$s.utils.CommonConstants;
                import %1$s.utils.PagedItems;
                import %1$s.utils.ResponseWrapper;
                import %1$s.utils.ServiceException;
                import io.swagger.v3.oas.annotations.Operation;
                import io.swagger.v3.oas.annotations.tags.Tag;
                import jakarta.validation.Valid;
                import org.springframework.beans.factory.annotation.Autowired;
                import org.springframework.http.HttpStatus;
                import org.springframework.http.ResponseEntity;
                import org.springframework.validation.annotation.Validated;
                import org.springframework.web.bind.annotation.CrossOrigin;
                import org.springframework.web.bind.annotation.DeleteMapping;
                import org.springframework.web.bind.annotation.GetMapping;
                import org.springframework.web.bind.annotation.PostMapping;
                import org.springframework.web.bind.annotation.PutMapping;
                import org.springframework.web.bind.annotation.RequestBody;
                import org.springframework.web.bind.annotation.RequestMapping;
                import org.springframework.web.bind.annotation.RequestParam;
                import org.springframework.web.bind.annotation.RestController;
                                
                import java.util.List;
                                
                @Validated
                @CrossOrigin
                @RestController
                @RequestMapping(value = %2$sConstants.PATH)
                @Tag(name = %2$sConstants.TAG, description = %2$sConstants.DESCRIPTION)
                public class %2$sController {
                                
                    private final I%2$sService service;
                                
                    @Autowired
                    public %2$sController(I%2$sService service) {
                        this.service = service;
                    }
                                
                    @Operation(summary = "Retrieve %2$s by ID")
                    @GetMapping(value = CommonConstants.RETRIEVE_BY_ID)
                    public ResponseEntity<ResponseWrapper<%2$sDto>> retrieveById(
                            @RequestParam(name = "%3$sId") final String %3$sId
                    ) throws ServiceException {
                        %2$sDto result = service.retrieveById(%3$sId);
                        return ApiResult.createSuccessResult(result, HttpStatus.OK);
                    }
                                
                    @Operation(summary = "Add a %2$s")
                    @PostMapping(value = CommonConstants.ADD_ENTITY)
                    public ResponseEntity<ResponseWrapper<%2$sDto>> add%2$s(
                            @RequestBody @Valid final %2$sDto %3$sDto
                    ) throws ServiceException {
                        %2$sDto result = service.add%2$s(%3$sDto);
                        return ApiResult.createSuccessResult(result, HttpStatus.CREATED);
                    }
                                
                    @Operation(summary = "Update a %2$s")
                    @PutMapping(value = CommonConstants.UPDATE_ENTITY)
                    public ResponseEntity<ResponseWrapper<%2$sDto>> update%2$s(
                            @RequestBody %2$sDto %3$sDto,
                            @RequestParam(name = "%3$sId") final String %3$sId
                    ) throws ServiceException {
                        %2$sDto result = service.update%2$s(%3$sDto, %3$sId);
                        return ApiResult.createSuccessResult(result, HttpStatus.OK);
                    }
                                
                    @Operation(summary = "Delete %2$s by ID")
                    @DeleteMapping(value = CommonConstants.DELETE_BY_ID)
                    public ResponseEntity<ResponseWrapper<String>> deleteById(
                            @RequestParam(name = "%3$sId") final String %3$sId,
                            @RequestParam(name = "hardDelete", required = false, defaultValue = "false") final boolean hardDelete
                    ) throws ServiceException {
                        String result = service.deleteById(%3$sId, hardDelete);
                        return ApiResult.createSuccessResult(result, HttpStatus.OK);
                    }
                                
                    @Operation(summary = "Retrieve all %2$ss (Paged)")
                    @GetMapping(value = CommonConstants.RETRIEVE_ALL_PAGED)
                    public ResponseEntity<ResponseWrapper<PagedItems<List<%2$sDto>>>> retrieveAll(
                            @RequestParam(name = "page", defaultValue = "1", required = false) final int pageNumber,
                            @RequestParam(name = "size", defaultValue = "10", required = false) final int pageSize,
                            @RequestParam(name = "includeSoftDeleted", defaultValue = "false", required = false) final boolean includeSoftDeleted
                    ) throws ServiceException {
                        PagedItems<List<%2$sDto>> result = service.retrieveAll(pageNumber, pageSize, includeSoftDeleted);
                        return ApiResult.createSuccessResult(result, HttpStatus.OK);
                    }
                                
                    @Operation(summary = "Search %2$ss (Paged)")
                    @PostMapping(value = CommonConstants.SEARCH_PAGED)
                    public ResponseEntity<ResponseWrapper<PagedItems<List<%2$sDto>>>> search%2$s(
                            @RequestBody %2$sDto %3$sDto,
                            @RequestParam(name = "page", defaultValue = "1", required = false) final int pageNumber,
                            @RequestParam(name = "size", defaultValue = "10", required = false) final int pageSize,
                            @RequestParam(name = "includeSoftDeleted", defaultValue = "false", required = false) final boolean includeSoftDeleted
                    ) throws ServiceException {
                        PagedItems<List<%2$sDto>> result = service.search%2$s(%3$sDto, pageNumber, pageSize, includeSoftDeleted);
                        return ApiResult.createSuccessResult(result, HttpStatus.OK);
                    }
                }
                """.formatted(projectDto.combineGroupAndArtifactId(), entity.getEntityName(), entity.lowerFirstLetter());

            AutoCodeGeneratorService.addFileInZip(zipOutputStream, filepath, content);
        }
    }
}
