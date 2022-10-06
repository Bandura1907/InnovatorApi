package com.innovator.innovator.payload.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VideoMetadataRequest {
    private Integer id;
    private String description;
    private String contentType;
    private String streamUrl;
}
