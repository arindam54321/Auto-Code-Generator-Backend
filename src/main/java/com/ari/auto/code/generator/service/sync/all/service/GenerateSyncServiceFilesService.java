package com.ari.auto.code.generator.service.sync.all.service;

import com.ari.auto.code.generator.dto.EntityDto;
import com.ari.auto.code.generator.dto.FieldDto;
import com.ari.auto.code.generator.dto.ProjectDto;
import com.ari.auto.code.generator.service.AutoCodeGeneratorService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.zip.ZipOutputStream;

@Service
public class GenerateSyncServiceFilesService {

    public void generateFiles(ZipOutputStream zipOutputStream, String rootPathForOther, ProjectDto projectDto) throws IOException {
        for (EntityDto entity : projectDto.getEntities()) {
            this.generateSyncIService(zipOutputStream, rootPathForOther, projectDto, entity);
            this.generateSyncService(zipOutputStream, rootPathForOther, projectDto, entity);
            this.generateSyncServiceConfig(zipOutputStream, rootPathForOther, projectDto, entity);
        }
    }

    private void generateSyncIService(ZipOutputStream zipOutputStream, String rootPathForOther, ProjectDto projectDto, EntityDto entity) throws IOException {
        String className = "I" + entity.getEntityName() + "Service";
        String filepath = rootPathForOther + "service/" + className + ".java";
        String content = """
                package %1$s.sync.service;
                                
                import %1$s.common.dto.%2$sDto;
                import %1$s.utils.PagedItems;
                import %1$s.utils.ServiceException;
                                
                import java.util.List;
                                
                public interface I%2$sService {
                    %2$sDto add%2$s(%2$sDto %3$sDto) throws ServiceException;
                                
                    PagedItems<List<%2$sDto>> retrieveAll(int pageNumber, int pageSize, boolean includeSoftDeleted) throws ServiceException;
                                
                    PagedItems<List<%2$sDto>> search%2$s(%2$sDto %3$sDto, int pageNumber, int pageSize, boolean includeSoftDeleted) throws ServiceException;
                                
                    %2$sDto retrieveById(String %3$sId) throws ServiceException;
                                
                    String deleteById(String %3$sId, boolean hardDelete) throws ServiceException;
                                
                    %2$sDto update%2$s(%2$sDto %3$sDto, String %3$sId) throws ServiceException;
                }
                """.formatted(projectDto.combineGroupAndArtifactId(), entity.getEntityName(), entity.lowerFirstLetter());

        AutoCodeGeneratorService.addFileInZip(zipOutputStream, filepath, content);
    }

    private void generateSyncService(ZipOutputStream zipOutputStream, String rootPathForOther, ProjectDto projectDto, EntityDto entity) throws IOException {
        String className = entity.getEntityName() + "Service";
        String filepath = rootPathForOther + "service/" + className + ".java";
        
        String content = """
                package %1$s.sync.service;
                                
                import %1$s.common.dto.%2$sDto;
                import %1$s.common.key.%2$sId;
                import %1$s.common.mapper.%2$sMapper;
                import %1$s.common.model.%2$s;
                import %1$s.sync.repository.%2$sRepository;
                import %1$s.utils.PagedItems;
                import %1$s.utils.ServiceException;
                import org.springframework.beans.factory.annotation.Autowired;
                import org.springframework.data.domain.PageRequest;
                import org.springframework.data.domain.Pageable;
                import org.springframework.data.mongodb.core.MongoTemplate;
                import org.springframework.data.mongodb.core.query.Criteria;
                import org.springframework.data.mongodb.core.query.Query;
                import org.springframework.http.HttpStatus;
                                
                import java.util.List;
                import java.util.Optional;
                                
                public class %2$sService implements I%2$sService {
                                
                    private final %2$sRepository repository;
                    private final MongoTemplate template;
                                
                    @Autowired
                    public %2$sService(%2$sRepository repository, MongoTemplate template) {
                        this.repository = repository;
                        this.template = template;
                    }
                
                %3$s
                
                %4$s
                
                %5$s
                
                %6$s}
                """.formatted(projectDto.combineGroupAndArtifactId(), 
                entity.getEntityName(),
                this.addEntityMethod(entity),
                this.retrieveAllAndSearchMethod(entity),
                this.retrieveAndDeleteByIdMethod(entity), 
                updateEntity(entity));

        AutoCodeGeneratorService.addFileInZip(zipOutputStream, filepath, content);
    }
    
    private String addEntityMethod(EntityDto entity) {
        StringBuilder uniqueKeyExclusion = new StringBuilder();
        for (FieldDto field : entity.getFields()) {
            if (field.isUnique()) {
                uniqueKeyExclusion.append("""
                                if (repository.%1$s(%2$s.%3$s(), false)) {
                                    throw new ServiceException("Failure: %4$s with same %5$s already exists!");
                                }
                        """.formatted(field.getExistByMethodName(), entity.lowerFirstLetter(), field.getGetterMethodName(), entity.getEntityName(), field.getFieldName()));
            }
        }
        String content = """
                    @Override
                    public %1$sDto add%1$s(%1$sDto %2$s) throws ServiceException {
                %3$s
                        %2$s.set%1$sId(%1$sId.generate());
                        return %1$sMapper.toDto(repository.save(%1$sMapper.toEntity(%2$s)));
                    }
                """.formatted(entity.getEntityName(), entity.lowerFirstLetter(), uniqueKeyExclusion);

        return content;
    }

    private String retrieveAllAndSearchMethod(EntityDto entity) {
        StringBuilder fieldInsertions = new StringBuilder("""
                        if (searchCriteria.get%1$sId() != null) {
                            query.addCriteria(Criteria.where("%2$sId").is(searchCriteria.get%1$sId()));
                        }
                """.formatted(entity.getEntityName(), entity.lowerFirstLetter()));
        
        for (FieldDto field : entity.getFields()) {
            if (!field.isSensitive()) {
                fieldInsertions.append("""
                                
                                if (searchCriteria.%1$s() != null) {
                                    query.addCriteria(Criteria.where("%2$s").is(searchCriteria.%1$s()));
                                }
                        """.formatted(field.getGetterMethodName(), field.getFieldName()));
            }
        }
        
        String content = """
                    @Override
                    public PagedItems<List<%1$sDto>> retrieveAll(
                            int pageNumber, int pageSize, boolean includeSoftDeleted
                    ) throws ServiceException {
                        return search%1$s(new %1$sDto(), pageNumber, pageSize, includeSoftDeleted);
                    }
                                
                    @Override
                    public PagedItems<List<%1$sDto>> search%1$s(
                            %1$sDto searchCriteria,
                            int pageNumber, int pageSize, boolean includeSoftDeleted
                    ) throws ServiceException {
                        Query query = new Query();
                
                %3$s
                        if (!includeSoftDeleted) {
                            query.addCriteria(Criteria.where("softDeleted").is(false));
                        }
                
                        long totalCount = template.count(query, %1$s.class);
                        pageNumber = Math.max(1, Math.min(pageNumber, (int) (totalCount + pageSize - 1) / pageSize));
                                
                        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
                        query.with(pageable);
                
                        var data = template.find(query, %1$s.class).stream().map(%1$sMapper::toSecuredDto).toList();
                
                        if (data.isEmpty()) {
                            throw new ServiceException("Failure: No Records", HttpStatus.NOT_FOUND);
                        }
                
                        return PagedItems.createPage(data, pageNumber, pageSize, totalCount);
                    }
                """.formatted(entity.getEntityName(), entity.lowerFirstLetter(), fieldInsertions);

        return content;
    }
    
    private String retrieveAndDeleteByIdMethod(EntityDto entity) {
        String content = """
                    @Override
                    public %1$sDto retrieveById(String %2$sId) throws ServiceException {
                        Optional<%1$s> %2$s = repository.findById(%2$sId);
                                
                        if (%2$s.isEmpty() || %2$s.get().isSoftDeleted()) {
                            throw new ServiceException("Failure: Invalid ID", HttpStatus.NOT_FOUND);
                        }
                                
                        return %1$sMapper.toDto(%2$s.get());
                    }
                                
                    @Override
                    public String deleteById(String %2$sId, boolean hardDelete) throws ServiceException {
                        Optional<%1$s> %2$s = repository.findById(%2$sId);
                        if (%2$s.isEmpty()) {
                            throw new ServiceException("Failure: Invalid ID", HttpStatus.NOT_FOUND);
                        }
                                
                        if (hardDelete) {
                            throw new ServiceException("Failure: Permission Denied!", HttpStatus.UNAUTHORIZED);
                            // repository.deleteById(%2$sId);
                            // return "Success: %1$s deleted with ID = %%s".formatted(%2$sId);
                        }
                                
                        if (%2$s.get().isSoftDeleted()) {
                            throw new ServiceException("Failure: Invalid ID", HttpStatus.NOT_FOUND);
                        }
                                
                        %1$s %2$sData = %2$s.get();
                        %2$sData.setSoftDeleted(true);
                        repository.save(%2$sData);
                        return "Success: %1$s deleted with ID = %%s".formatted(%2$sId);
                    }
                """.formatted(entity.getEntityName(), entity.lowerFirstLetter());
        return content;
    }
    
    private String updateEntity(EntityDto entity) {
        StringBuilder uniqueKeyExclusions = new StringBuilder();
        StringBuilder fieldUpdates = new StringBuilder();
        
        for (FieldDto field : entity.getFields()) {
            if (field.isUnique()) {
                uniqueKeyExclusions.append("""
                                
                                if (%1$sDto.%2$s() != null) {
                                    throw new ServiceException("Failure: %3$s can't be updated", HttpStatus.BAD_REQUEST);
                                }
                        """.formatted(entity.lowerFirstLetter(), field.getGetterMethodName(), field.toFirstCaseCapitalized()));
            } else {
                fieldUpdates.append("""
                                
                                if (%1$sDto.%2$s() != null) {
                                    old%3$sDto.%4$s(%1$sDto.%2$s());
                                }
                        """.formatted(entity.lowerFirstLetter(), field.getGetterMethodName(), entity.getEntityName(), field.getSetterMethodName()));
            }
        }
        
        String content = """
                @Override
                    public %1$sDto update%1$s(%1$sDto %2$sDto, String %2$sId) throws ServiceException {
                        if (%2$sDto.get%1$sId() == null) {
                            throw new ServiceException("Failure: %1$sId should be provided in payload.", HttpStatus.BAD_REQUEST);
                        } else if (!%2$sId.equals(%2$sDto.get%1$sId())) {
                            throw new ServiceException("Failure: %1$sId should be same in payload and parameter.", HttpStatus.BAD_REQUEST);
                        } else if (!repository.existsById(%2$sId)) {
                            throw new ServiceException("Failure: Invalid %1$sId", HttpStatus.NOT_FOUND);
                        }
                %3$s
                
                        %1$s %2$s = repository.findById(%2$sId).get();
                        %1$sDto old%1$sDto = %1$sMapper.toDto(%2$s);
                %4$s
                
                        %1$s updated%1$s = repository.save(%1$sMapper.toEntity(old%1$sDto));
                
                        return %1$sMapper.toSecuredDto(updated%1$s);
                    }
                """.formatted(entity.getEntityName(), entity.lowerFirstLetter(), uniqueKeyExclusions, fieldUpdates);
        
        return content;
    }
    
    private void generateSyncServiceConfig(ZipOutputStream zipOutputStream, String rootPathForOther, ProjectDto projectDto, EntityDto entity) throws IOException {
        String className = entity.getEntityName() + "ServiceConfig";
        String filepath = rootPathForOther + "service/config/" + className + ".java";

        String content = """
                package %1$s.sync.service.config;
                                
                import %1$s.sync.repository.%2$sRepository;
                import %1$s.sync.service.I%2$sService;
                import %1$s.sync.service.%2$sService;
                import org.springframework.beans.factory.annotation.Autowired;
                import org.springframework.context.annotation.Bean;
                import org.springframework.context.annotation.Configuration;
                import org.springframework.data.mongodb.core.MongoTemplate;
                                
                @Configuration
                public class %2$sServiceConfig {
                    @Autowired
                    private %2$sRepository %3$sRepository;
                                
                    @Autowired
                    private MongoTemplate mongoTemplate;
                                
                    @Bean
                    public I%2$sService get%2$sService() {
                        return new %2$sService(%3$sRepository, mongoTemplate);
                    }
                }
                """.formatted(projectDto.combineGroupAndArtifactId(), entity.getEntityName(), entity.lowerFirstLetter());

        AutoCodeGeneratorService.addFileInZip(zipOutputStream, filepath, content);
    }
}
