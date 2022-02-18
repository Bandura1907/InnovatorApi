package com.innovator.innovator.controllers.repr;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VideoMetadataRepr {
    private Integer id;
    private String description;
    private String contentType;
    private String streamUrl;
}
