package com.ari.auto.code.generator.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntityDto {
    @Schema(description = "Entity Name", example = "UserDetail")
    @Pattern(regexp = "^[A-Z][a-zA-Z0-9]*$", message = "Invalid pattern for EntityName. Example of correct pattern: 'UserDetail'")
    private String entityName;

    @Valid
    private List<FieldDto> fields;

    @Schema(hidden = true)
    public boolean hasListFields() {
        for (FieldDto i : fields) {
            if (i.getDataType().getType().startsWith("List")) {
                return true;
            }
        }
        return false;
    }

    @Schema(hidden = true)
    public String toHyphenDelimited() {
        if (this.entityName == null || this.entityName.isEmpty()) {
            return this.entityName;
        }

        String[] words = this.entityName.split("(?<!^)(?=[A-Z])");
        StringBuilder result = new StringBuilder();

        for (String word : words) {
            result.append(word.toLowerCase()).append("-");
        }

        if (result.length() > 0) {
            result.setLength(result.length() - 1);
        }

        return result.toString();
    }

    @Schema(hidden = true)
    public String lowerFirstLetter() {
        return this.entityName.substring(0, 1).toLowerCase() + this.entityName.substring(1);
    }
}
