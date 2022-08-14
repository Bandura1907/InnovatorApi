package com.innovator.innovator.controllers;

import com.innovator.innovator.models.ERole;
import com.innovator.innovator.models.Role;
import com.innovator.innovator.models.UserAuth;
import com.innovator.innovator.payload.request.LoginRequest;
import com.innovator.innovator.payload.request.RegisterRequest;
import com.innovator.innovator.payload.response.JwtResponse;
import com.innovator.innovator.repository.RoleRepository;
import com.innovator.innovator.security.jwt.JwtUtils;
import com.innovator.innovator.security.services.UserDetailsImpl;
import com.innovator.innovator.security.services.UserDetailsServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private AuthenticationManager authenticationManager;
    private JwtUtils jwtUtils;
    private UserDetailsServiceImpl userDetailsService;
    private PasswordEncoder passwordEncoder;
    private RoleRepository roleRepository;

    @GetMapping("/users")
    public ResponseEntity<List<UserAuth>> getAllUsersAuth() {
        return ResponseEntity.ok(userDetailsService.findAll());
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserAuth> getUser(@PathVariable Integer id) {
        Optional<UserAuth> userAuth = userDetailsService.findById(id);
        if (userAuth.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(userAuth.get());
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String roles = new ArrayList<>(userDetails.getAuthorities()).get(0).toString();

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                roles));
    }

    @PostMapping("/create")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest body) {
        if (userDetailsService.existsByUsername(body.getUsername())) {
            return ResponseEntity.badRequest()
                    .body("User " + body.getUsername() + " already register");
        }

        UserAuth userAuth = new UserAuth();
        userAuth.setUsername(body.getUsername());
        userAuth.setPassword(passwordEncoder.encode(body.getPassword()));

        Set<Role> roles = userSetRoles(body.getRole());

        userAuth.setRoles(roles);
        userDetailsService.save(userAuth);

        return ResponseEntity.ok("User register");
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<?> editUser(@RequestBody RegisterRequest body, @PathVariable Integer id) {

        UserAuth userAuth = userDetailsService.findById(id).orElseThrow(NotFoundException::new);
        userAuth.setUsername(body.getUsername());
        userAuth.setPassword(body.getPassword() == null ? userAuth.getPassword() : passwordEncoder.encode(body.getPassword()));

        Set<Role> roles = userSetRoles(body.getRole());
        userAuth.setRoles(roles);
//        if (!userDetailsService.existsByUsername(body.getUsername()))
        userDetailsService.save(userAuth);
        return ResponseEntity.ok("User change");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userDetailsService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    private Set<Role> userSetRoles(Set<String> strRoles) {
        Set<Role> roles = new HashSet<>();

        if (strRoles == null || strRoles.size() == 0) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER).orElseThrow(() ->
                    new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                if ("admin".equals(role)) {
                    Role roleAdmin = roleRepository.findByName(ERole.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(roleAdmin);
                } else if ("manager".equals(role)) {
                    Role roleManager = roleRepository.findByName(ERole.ROLE_MANAGER)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(roleManager);
                } else {
                    Role roleUser = roleRepository.findByName(ERole.ROLE_USER)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(roleUser);
                }

            });
        }
        return roles;
    }
}
