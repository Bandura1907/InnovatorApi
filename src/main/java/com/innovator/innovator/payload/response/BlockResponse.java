package com.innovator.innovator.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class BlockResponse {

    private String name;
    private String description;

    @JsonProperty("max_length")
    private int maxLength;

    @JsonProperty("is_default")
    private boolean isDefault;
}
