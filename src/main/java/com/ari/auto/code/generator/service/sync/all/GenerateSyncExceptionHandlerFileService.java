package com.ari.auto.code.generator.service.sync.all;

import com.ari.auto.code.generator.dto.ProjectDto;
import com.ari.auto.code.generator.service.AutoCodeGeneratorService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.zip.ZipOutputStream;

@Service
public class GenerateSyncExceptionHandlerFileService {


    public void generateFiles(ZipOutputStream zipOutputStream, String rootPathForOther, ProjectDto projectDto) throws IOException {
        String filepath = rootPathForOther + "CustomExceptionHandler.java";
        String content = """
                package %1$s.sync;
                                
                import %1$s.utils.ApiResult;
                import %1$s.utils.ResponseWrapper;
                import %1$s.utils.ServiceException;
                import jakarta.validation.ConstraintViolation;
                import jakarta.validation.ConstraintViolationException;
                import org.springframework.context.support.DefaultMessageSourceResolvable;
                import org.springframework.http.HttpStatus;
                import org.springframework.http.ResponseEntity;
                import org.springframework.web.bind.MethodArgumentNotValidException;
                import org.springframework.web.bind.annotation.ExceptionHandler;
                import org.springframework.web.bind.annotation.RestControllerAdvice;
                                
                import java.util.List;
                import java.util.stream.Collectors;
                                
                @RestControllerAdvice
                public class CustomExceptionHandler {
                                
                    @ExceptionHandler(ServiceException.class)
                    public ResponseEntity<ResponseWrapper<String>> handleServiceException(ServiceException e) {
                        String message = (e.getMessage() == null || e.getMessage().isEmpty()) ? "Unexpected Service Exception" : e.getMessage();
                        HttpStatus status = (e.getStatus() == null || !e.getStatus().isError()) ? HttpStatus.INTERNAL_SERVER_ERROR : e.getStatus();
                        return ApiResult.createFailureResult(message, status);
                    }
                                
                    @ExceptionHandler(MethodArgumentNotValidException.class)
                    public ResponseEntity<ResponseWrapper<List<String>>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
                        List<String> data = e.getFieldErrors()
                                .stream()
                                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                                .collect(Collectors.toList());
                        HttpStatus status = HttpStatus.BAD_REQUEST;
                        return ApiResult.createFailureResult(data, status);
                    }
                                
                    @ExceptionHandler(ConstraintViolationException.class)
                    public ResponseEntity<ResponseWrapper<List<String>>> handleConstraintViolationException(ConstraintViolationException e) {
                        List<String> data = e.getConstraintViolations()
                                .stream()
                                .map(ConstraintViolation::getMessage)
                                .toList();
                        HttpStatus status = HttpStatus.BAD_REQUEST;
                        return ApiResult.createFailureResult(data, status);
                    }
                                
                    @ExceptionHandler(Exception.class)
                    public ResponseEntity<ResponseWrapper<String>> handleException(Exception e) {
                        String message = (e.getMessage() == null || e.getMessage().isEmpty())
                                ? "Unknown Exception! Please contact the developers!"
                                : e.getMessage();
                        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
                        return ApiResult.createFailureResult(message, status);
                    }
                }
                """.formatted(projectDto.combineGroupAndArtifactId());
        AutoCodeGeneratorService.addFileInZip(zipOutputStream, filepath, content);
    }
}
