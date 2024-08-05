package com.ari.auto.code.generator.dto;

import com.ari.auto.code.generator.enums.DataType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FieldDto {
    @Schema(description = "Field Name", example = "exampleFieldName")
    @Pattern(regexp = "^(?i)(?!is).*", message = "Field name can't be starting with 'is'")
    @Pattern(regexp = "^[a-z]+([A-Z][a-zA-Z0-9]*)*$", message = "Invalid patter for FieldName. Example of correct pattern: 'exampleFieldName'")
    private String fieldName;

    private DataType dataType;

    @Schema(description = "Is the field a Unique Key?", example = "false")
    private boolean isUnique;

    @Schema(description = "Does the field contain Sensitive data?", example = "false")
    private boolean isSensitive;

    @Schema(hidden = true)
    public String getGetterMethodName() {
        String getterMethodName = "get";
        getterMethodName += this.toFirstCaseCapitalized();
        return getterMethodName;
    }

    @Schema(hidden = true)
    public String getSetterMethodName() {
        String setterMethodName = "set";
        setterMethodName += this.toFirstCaseCapitalized();
        return setterMethodName;
    }

    @Schema(hidden = true)
    public String toFirstCaseCapitalized() {
        return this.fieldName.substring(0, 1).toUpperCase() + this.fieldName.substring(1);
    }

    @Schema(hidden = true)
    public String getExistByMethodName() {
        return "existsBy" + this.toFirstCaseCapitalized() +"AndSoftDeleted";
    }
}
