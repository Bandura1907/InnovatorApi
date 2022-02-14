package com.innovator.innovator.controllers;

import com.innovator.innovator.models.Useful;
import com.innovator.innovator.models.UserAuth;
import com.innovator.innovator.repository.UsefulRepository;
import com.innovator.innovator.repository.UserAuthRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class UsefulController {

    private UsefulRepository usefulRepository;
    private UserAuthRepository userAuthRepository;

    @GetMapping("/useful")
    public ResponseEntity<List<Useful>> getUsefulAll() {
        return  ResponseEntity.ok(usefulRepository.findAll());
    }

    @GetMapping("/useful/{id}")
    public ResponseEntity<Useful> getUseful(@PathVariable Integer id) {
        Optional<Useful> useful = usefulRepository.findById(id);

        if (useful.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(useful.get());
    }

    @PostMapping("/useful")
    public ResponseEntity<Useful> addUseful(@RequestBody Useful useful) {
        UserAuth userAuth = userAuthRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).get();
        useful.setUser(userAuth);
        return ResponseEntity.ok(usefulRepository.save(useful));
    }

    @PutMapping("/useful/{id}")
    public ResponseEntity<Useful> editUseful(@PathVariable Integer id, @RequestBody Useful useful) {
        Optional<Useful> usefulOptional = usefulRepository.findById(id);

        if (usefulOptional.isEmpty())
            return ResponseEntity.notFound().build();

        usefulOptional.get().setTitle(useful.getTitle());
        usefulOptional.get().setDescription(useful.getDescription());
        usefulOptional.get().setCategory(useful.getCategory());
        usefulOptional.get().setImageUrl(useful.getImageUrl());


        return ResponseEntity.ok(usefulRepository.save(usefulOptional.get()));
    }
}
