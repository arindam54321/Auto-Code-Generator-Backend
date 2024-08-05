package com.ari.auto.code.generator.controller;

import com.ari.auto.code.generator.ApiResult;
import com.ari.auto.code.generator.ResponseWrapper;
import com.ari.auto.code.generator.dto.ProjectDto;
import com.ari.auto.code.generator.service.AutoCodeGeneratorService;
import com.ari.auto.code.generator.service.ValidateInputsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping(value = "api/v1/acg")
@Tag(name = "Auto Code Generator", description = "Generate basic MVC for your project")
public class AutoCodeGeneratorController {

    @Autowired
    private AutoCodeGeneratorService service;
    @Autowired
    private ValidateInputsService validateInputsService;

    @PostMapping(value = "generate")
    @Operation(summary = "Download code as zip")
    ResponseEntity<byte[]> generate(
            @Valid @RequestBody ProjectDto projectDto
    ) throws Exception {
        validateInputsService.validate(projectDto);

        byte[] zipBytes = service.generate(projectDto);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", projectDto.getProjectName() + ".zip");

        return new ResponseEntity<>(zipBytes, headers, HttpStatus.OK);
    }

    @PostMapping(value = "validate")
    @Operation(summary = "Validate the inputs")
    ResponseEntity<ResponseWrapper<String>> validate(
            @Valid @RequestBody ProjectDto projectDto
    ) throws Exception {
        String response = validateInputsService.validate(projectDto);
        return ApiResult.createSuccessResult(response, HttpStatus.OK);
    }
}
