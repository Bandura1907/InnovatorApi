package com.innovator.innovator.payload.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
public class NewVideoRequest {
    private MultipartFile file;
}
