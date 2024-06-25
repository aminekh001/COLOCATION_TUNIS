package com.example.demo.auth;


import com.example.demo.role.RoleRepo;
import com.example.demo.user.Token;
import com.example.demo.user.TokenRepo;
import com.example.demo.user.User;
import com.example.demo.user.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;
    private  final UserRepo userRepo;
    private final TokenRepo tokenRepo;




    public void register(RegisterRequest request) {
        var userRole= roleRepo.findByName("USER")
                //todo-exception handling
                .orElseThrow(()->new IllegalStateException("Role User was not initialized"));
        var user= User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .phone(request.getPhone())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .accountLocked(false)
                .enabled(false)
                .roles(List.of(userRole))
                .build();
        userRepo.save(user);
        sendValidationEmail(user);
    }

    private void sendValidationEmail(User user) {
        var newToken=generateAndSaveActivationToken(user);
        //send email
        
    }

    private String generateAndSaveActivationToken(User user) {
        //generate Token
        String generatedToken=generateActivationCode(6);
        var token= Token.builder()
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();
        tokenRepo.save(token);
        return generatedToken;
    }

    private String generateActivationCode(int length) {
        String characters ="0123456789";
        StringBuilder codeBuilder = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();
        for (int i=0; i<length;i++){
            int randomIndex = secureRandom.nextInt(characters.length());//0..9
            codeBuilder.append(characters.charAt(randomIndex));
        }
        return codeBuilder.toString();
    }
}
