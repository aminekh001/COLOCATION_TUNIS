package com.example.demo.auth;


import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
@Tag(name="Authentication")
public class AuthController {
private final  AuthService service;

@PostMapping("/auth/register")
@ResponseStatus(HttpStatus.ACCEPTED)
public ResponseEntity<?> resgister(
        @RequestBody @Valid RegisterRequest request
) throws MessagingException {
    service.register(request);
    return  ResponseEntity.accepted().build();
}
}
