package com.innovator.innovator.controllers.repr;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
public class NewVideoRepr {

    private MultipartFile file;
}
