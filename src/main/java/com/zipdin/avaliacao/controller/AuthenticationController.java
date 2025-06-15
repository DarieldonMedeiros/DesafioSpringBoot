package com.zipdin.avaliacao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zipdin.avaliacao.dto.AuthenticationDTO;
import com.zipdin.avaliacao.dto.LoginResponseDTO;
import com.zipdin.avaliacao.dto.RegisterDTO;
import com.zipdin.avaliacao.entities.UserEntity;
import com.zipdin.avaliacao.repository.UserRepository;
import com.zipdin.avaliacao.services.TokenService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("auth")
@Tag(name = "Authentication")
public class AuthenticationController {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Realiza o Login do usuário já cadastrado")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Login realizado com sucesso!"),
        @ApiResponse(responseCode = "400", description = "Login ou senha incorretos"),
        @ApiResponse(responseCode = "500", description = "Ocorreu um erro inesperado"),
    })
    public ResponseEntity<Object> login(@RequestBody @Valid AuthenticationDTO data){
        var UsernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        var auth = this.authenticationManager.authenticate(UsernamePassword);

        var token = tokenService.generateToken((UserEntity) auth.getPrincipal());
        
        return ResponseEntity.status(HttpStatus.OK).body(new LoginResponseDTO(token));
    }

    @PostMapping("/register")
    @Operation(summary = "Register", description = "Realiza o cadastro do usuário")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Cadastro realizado com sucesso!"),
        @ApiResponse(responseCode = "400", description = "Login ja cadastrado"),
        @ApiResponse(responseCode = "500", description = "Ocorreu um erro inesperado"),
    })
    public ResponseEntity<Object> register(@RequestBody @Valid RegisterDTO data){
        if(this.userRepository.findByLogin(data.login()) != null) return ResponseEntity.badRequest().build();

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        UserEntity newUser = new UserEntity(data.login(), encryptedPassword, data.role());

        this.userRepository.save(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

}
