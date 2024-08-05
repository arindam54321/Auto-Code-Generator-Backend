package com.ari.auto.code.generator.service.utils.all;

import com.ari.auto.code.generator.dto.ProjectDto;
import com.ari.auto.code.generator.service.AutoCodeGeneratorService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.zip.ZipOutputStream;

@Service
public class GenerateUtilsOtherFileService {

    public void generateFiles(ZipOutputStream zipOutputStream, String rootPathForOther, ProjectDto projectDto) throws IOException {
        String __package = projectDto.combineGroupAndArtifactId() + ".utils";
        this.generateApiResultFile(zipOutputStream, rootPathForOther, __package);
        this.generateCommonConstantsFile(zipOutputStream, rootPathForOther, __package);
        this.generatePagedItemsFile(zipOutputStream, rootPathForOther, __package);
        this.generateResponseWrapperFile(zipOutputStream, rootPathForOther, __package);
        this.generateServiceExceptionFile(zipOutputStream, rootPathForOther, __package);
    }

    private void generateApiResultFile(ZipOutputStream zipOutputStream, String rootPathForOther, String __package) throws IOException {
        String filepath = rootPathForOther + "ApiResult.java";
        String content = """
                package %1$s;
                                
                import org.springframework.http.HttpStatus;
                import org.springframework.http.ResponseEntity;
                                
                import java.util.LinkedHashMap;
                                
                public class ApiResult<TResult> {
                    private static final String SUCCESS = "SUCCESS";
                    private static final String FAILURE = "FAILURE";
                                
                    public static <TResult> ResponseEntity<ResponseWrapper<TResult>> createSuccessResult(TResult result, HttpStatus status) {
                        return ResponseEntity.status(status).body(ResponseWrapper.wrap(SUCCESS, result, status));
                    }
                                
                    public static <TResult> ResponseEntity<ResponseWrapper<TResult>> createSuccessResult(TResult result, HttpStatus status, LinkedHashMap<String, Object> additionalInformation) {
                        return ResponseEntity.status(status).body(ResponseWrapper.wrap(SUCCESS, result, status, additionalInformation));
                    }
                                
                    public static <TResult> ResponseEntity<ResponseWrapper<TResult>> createFailureResult(TResult result, HttpStatus status) {
                        return ResponseEntity.status(status).body(ResponseWrapper.wrap(FAILURE, result, status));
                    }
                                
                    public static <TResult> ResponseEntity<ResponseWrapper<TResult>> createFailureResult(TResult result, HttpStatus status, LinkedHashMap<String, Object> additionalInformation) {
                        return ResponseEntity.status(status).body(ResponseWrapper.wrap(FAILURE, result, status, additionalInformation));
                    }
                }
                """.formatted(__package);

        AutoCodeGeneratorService.addFileInZip(zipOutputStream, filepath, content);
    }

    private void generateCommonConstantsFile(ZipOutputStream zipOutputStream, String rootPathForOther, String __package) throws IOException {
        String filepath = rootPathForOther + "CommonConstants.java";
        String content = """
                package %1$s;
                
                public class CommonConstants {
                    public static final String ADD_ENTITY = "add";
                    public static final String RETRIEVE_BY_ID = "retrieve";
                    public static final String DELETE_BY_ID = "delete";
                    public static final String RETRIEVE_ALL_PAGED = "retrieve-all";
                    public static final String SEARCH_PAGED = "search";
                    public static final String UPDATE_ENTITY = "update";
                }
                """.formatted(__package);

        AutoCodeGeneratorService.addFileInZip(zipOutputStream, filepath, content);
    }

    private void generatePagedItemsFile(ZipOutputStream zipOutputStream, String rootPathForOther, String __package) throws IOException {
        String filepath = rootPathForOther + "PagedItems.java";
        String content = """
                package %1$s;
                                
                import lombok.AllArgsConstructor;
                import lombok.Builder;
                import lombok.Data;
                import lombok.NoArgsConstructor;
                                
                                
                @Data
                @Builder
                @NoArgsConstructor
                @AllArgsConstructor
                public class PagedItems<TResult> {
                    TResult pagedItems;
                    int page;
                    int size;
                    long totalCount;
                                
                    public static <TResult> PagedItems<TResult> createPage(
                            TResult pagedItems,
                            int page, int size, long totalCount
                    ) {
                        return new PagedItems<TResult>(pagedItems, page, size, totalCount);
                    }
                }
                """.formatted(__package);

        AutoCodeGeneratorService.addFileInZip(zipOutputStream, filepath, content);
    }

    private void generateResponseWrapperFile(ZipOutputStream zipOutputStream, String rootPathForOther, String __package) throws IOException {
        String filepath = rootPathForOther + "ResponseWrapper.java";
        String content = """
                package %1$s;
                                
                import lombok.AllArgsConstructor;
                import lombok.Builder;
                import lombok.Data;
                import lombok.NoArgsConstructor;
                import org.springframework.http.HttpStatus;
                                
                import java.util.LinkedHashMap;
                                
                @Data
                @Builder
                @NoArgsConstructor
                @AllArgsConstructor
                public class ResponseWrapper<TResult> {
                    String type;
                    HttpStatus status;
                    TResult result;
                    LinkedHashMap<String, Object> additionalInformation;
                                
                    public static <TResult> ResponseWrapper<TResult> wrap(String type, TResult result, HttpStatus status) {
                        return new ResponseWrapper<TResult>(type, status, result, null);
                    }
                                
                    public static <TResult> ResponseWrapper<TResult> wrap(String type, TResult result, HttpStatus status, LinkedHashMap<String, Object> additionalInformation) {
                        return new ResponseWrapper<TResult>(type, status, result, additionalInformation);
                    }
                }
                """.formatted(__package);

        AutoCodeGeneratorService.addFileInZip(zipOutputStream, filepath, content);
    }

    private void generateServiceExceptionFile(ZipOutputStream zipOutputStream, String rootPathForOther, String __package) throws IOException {
        String filepath = rootPathForOther + "ServiceException.java";
        String content = """
                package %1$s;
                                
                import org.springframework.http.HttpStatus;
                                
                public class ServiceException extends Exception {
                    private HttpStatus status;
                                
                    public HttpStatus getStatus() {
                        return status;
                    }
                                
                    public ServiceException(String message) {
                        super(message);
                    }
                                
                    public ServiceException(HttpStatus status) {
                        super();
                        this.status = status;
                    }
                                
                    public ServiceException(String message, HttpStatus status) {
                        super(message);
                        this.status = status;
                    }
                }
                """.formatted(__package);

        AutoCodeGeneratorService.addFileInZip(zipOutputStream, filepath, content);
    }
}
