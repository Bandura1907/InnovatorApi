package com.innovator.innovator.payload.request;

import com.innovator.innovator.models.Blocks;
import lombok.Data;

import java.util.List;

@Data
public class BlockRequest {
    List<Blocks> blocks;
}
