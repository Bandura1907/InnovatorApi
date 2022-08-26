package com.innovator.innovator.controllers;

import com.innovator.innovator.models.Blocks;
import com.innovator.innovator.models.EType;
import com.innovator.innovator.models.Products;
import com.innovator.innovator.payload.request.BlockRequest;
import com.innovator.innovator.payload.response.BlockResponse;
import com.innovator.innovator.payload.response.MessageResponse;
import com.innovator.innovator.repository.BlockRepository;
import com.innovator.innovator.repository.ProductsRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class BlockController {

    private final BlockRepository blockRepository;
    private final ProductsRepository productsRepository;

    @GetMapping("/get_blocks/{index}")
    public ResponseEntity<?> getBlocks(@PathVariable int index) {
        Products products = getProductByIndex(index);
        if (products == null) {
            return new ResponseEntity<>(new MessageResponse("product not found"), HttpStatus.NOT_FOUND);
        }

        List<Blocks> blocks = products.getBlocks();
        List<BlockResponse> responses = new ArrayList<>();
        for (Blocks item : blocks) {
            responses.add(new BlockResponse(
                    item.getId(),
                    item.getName(),
                    item.getDescription(),
                    item.getDescription().length(),
                    true));
        }

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/get_block_by_id/{id}")
    public ResponseEntity<?> getBlockById(@PathVariable int id) {
        Optional<Blocks > blocks = blockRepository.findById(id);
        if (blocks.isEmpty())
            return new ResponseEntity<>(new MessageResponse("Block not found"), HttpStatus.NOT_FOUND);

        return ResponseEntity.ok(blocks.get());
    }

    @PostMapping("/add_block/{index}")
    public ResponseEntity<?> addBlock(@PathVariable int index, @RequestBody BlockRequest blockRequest) {
        Products products = getProductByIndex(index);
        Blocks block = new Blocks(blockRequest.getName(), blockRequest.getDescription(), products);

        blockRepository.save(block);

        return ResponseEntity.ok(new MessageResponse("Block add"));
    }

    @PutMapping("/edit_block/{id}/{index}")
    public ResponseEntity<?> editBlock(@PathVariable int id, @PathVariable int index,
                                       @RequestBody BlockRequest blockRequest) {
        Optional<Blocks> block = blockRepository.findById(id);
        if (block.isEmpty())
            return new ResponseEntity<>(new MessageResponse("Block not found"), HttpStatus.NOT_FOUND);

        Products products = getProductByIndex(index);

        block.get().setName(blockRequest.getName());
        block.get().setDescription(blockRequest.getDescription());
        block.get().setProducts(products);

        return ResponseEntity.ok(blockRepository.save(block.get()));
    }

    @DeleteMapping("/delete_block/{id}")
    public ResponseEntity<?> deleteBlock(@PathVariable int id) {
        blockRepository.deleteById(id);
        return ResponseEntity.ok(new MessageResponse("Block deleted"));
    }

//    @PutMapping("/add_block/{index}")
//    public ResponseEntity<?> addBlock(@PathVariable int index,
//                                      @RequestBody BlockRequest blockRequest) {
//        Products products = getProductByIndex(index);
//        if (products == null) {
//            return new ResponseEntity<>(new MessageResponse("product not found"), HttpStatus.NOT_FOUND);
//        }
//
//        blockRequest.getBlocks().forEach(x -> x.setProducts(products));
//
//        List<Blocks> blocks = products.getBlocks();
//        blocks.addAll(blockRequest.getBlocks());
//        products.setBlocks(blocks);
//
//        productsRepository.save(products);
//
//        return ResponseEntity.ok(new MessageResponse("Blocks successfully add"));
//    }

    private Products getProductByIndex(int index) {
        switch (index) {
            case 0:
                return productsRepository.findByType(EType.TECHNOLOGY);
            case 1:
                return productsRepository.findByType(EType.DIGITAL);
            case 2:
                return productsRepository.findByType(EType.INTERNET);
            case 3:
                return productsRepository.findByType(EType.PHYSICAL);
            default: return null;
        }
    }
}
