package com.ari.auto.code.generator.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Strings;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDto {
    @Schema(description = "Project Name", example = "ExampleProjectName")
    @Pattern(regexp = "^[A-Z][a-zA-Z0-9]*$", message = "Invalid patter for ProjectName. Example of correct pattern: 'ExampleProjectName'")
    private String projectName;

    @Schema(description = "Group Id", example = "com.example")
    @Pattern(regexp = "^[a-z]+(\\.[a-z]+)*$", message = "Invalid patter for GroupId. Example of correct pattern: 'com.example'")
    private String groupId;

    @Schema(description = "Artifact Id", example = "auto.code.generator")
    @Pattern(regexp = "^[a-z]+(\\.[a-z]+)*$", message = "Invalid patter for ArtifactId. Example of correct pattern: 'auto.code.generator'")
    private String artifactId;

    @Valid
    private List<EntityDto> entities;

    @Schema(hidden = true)
    public String artifactToHyphenDelimitedCamelCase() {
        String artifact = this.artifactId;

        String[] elements = artifact.split("\\.");
        List<String> newElements = new ArrayList<>();
        for (String i : elements) {
            newElements.add(i.substring(0, 1).toUpperCase() + i.substring(1));
        }

        return Strings.join(newElements, '-');
    }

    @Schema(hidden = true)
    public String artifactToHyphenDelimitedLowerCase() {
        String artifact = this.artifactId;
        return artifact.replace('.', '-');
    }

    @Schema(hidden = true)
    public String combineGroupAndArtifactId() {
        String group = this.groupId;
        String artifact = this.artifactId;
        return group + '.' + artifact;
    }

    @Schema(hidden = true)
    public String toHyphenDelimited() {
        if (this.projectName == null || this.projectName.isEmpty()) {
            return this.projectName;
        }

        String[] words = this.projectName.split("(?<!^)(?=[A-Z])");
        StringBuilder result = new StringBuilder();

        for (String word : words) {
            result.append(word.toLowerCase()).append("-");
        }

        if (result.length() > 0) {
            result.setLength(result.length() - 1);
        }

        return result.toString();
    }
}
