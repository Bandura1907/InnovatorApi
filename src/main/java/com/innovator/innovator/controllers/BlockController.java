package com.innovator.innovator.controllers;

import com.innovator.innovator.models.Blocks;
import com.innovator.innovator.models.EType;
import com.innovator.innovator.models.Products;
import com.innovator.innovator.payload.response.BlockResponse;
import com.innovator.innovator.payload.response.MessageResponse;
import com.innovator.innovator.repository.BlockRepository;
import com.innovator.innovator.repository.ProductsRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class BlockController {

    private final BlockRepository blockRepository;
    private final ProductsRepository productsRepository;

    @GetMapping("/get_blocks/{index}")
    public ResponseEntity<?> getBlocks(@PathVariable int index) {
        Products products;
        List<Blocks> blocks;
        switch (index) {
            case 0:
                products = productsRepository.findByType(EType.TECHNOLOGY);
                break;
            case 1:
                products = productsRepository.findByType(EType.DIGITAL);
                break;
            case 2:
                products = productsRepository.findByType(EType.INTERNET);
                break;
            case 3:
                products = productsRepository.findByType(EType.PHYSICAL);
                break;
            default: return new ResponseEntity<>(new MessageResponse("index not found"), HttpStatus.NOT_FOUND);
        }

        blocks = products.getBlocks();
        List<BlockResponse> responses = new ArrayList<>();
        for (Blocks item : blocks) {
            responses.add(new BlockResponse(item.getName(), item.getDescription(), item.getDescription().length(), true));
        }

        return ResponseEntity.ok(responses);
    }
}
