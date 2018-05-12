package com.jsonschema.validation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidationContext {

    private String triggerPoint;
    private String schemaId;
    private String schemaPath;
    private String payload;

    public String getTriggerPoint() {
        return triggerPoint;
    }

    public void setTriggerPoint(String triggerPoint) {
        if (this.triggerPoint != null) {
            return;
        }
        this.triggerPoint = triggerPoint;
    }

    public String getSchemaId() {
        return schemaId;
    }

    public void setSchemaId(String schemaId) {
        if (this.schemaId != null) {
            return;
        }
        this.schemaId = schemaId;
    }

    public String getSchemaPath() {
        return schemaPath;
    }

    public void setSchemaPath(String schemaPath) {
        if (this.schemaPath != null) {
            return;
        }
        this.schemaPath = schemaPath;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        if (this.payload != null) {
            return;
        }
        this.payload = payload;
    }
}
