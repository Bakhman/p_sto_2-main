package com.javamentor.qa.platform.webapp.controllers.rest;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.security.JwtAuthenticationProvider;
import com.javamentor.qa.platform.service.abstracts.model.RoleService;
import com.javamentor.qa.platform.models.dto.AuthenticationRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.Objects;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Api(value = "RestController", description = "Controller to authenticate user with JWT")
@ApiResponses(value = {
        @ApiResponse(code = 200, message = "Success|OK"),
        @ApiResponse(code = 400, message = "Bad Request"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 404, message = "Not found"),
        @ApiResponse(code = 500, message = "Internal error")
})
@RestController
@RequestMapping("/api")
public class AuthenticationResourceController {
    private final RoleService roleService;
    private final JwtAuthenticationProvider authenticationProvider;

    public AuthenticationResourceController(JwtAuthenticationProvider provider,
                                            RoleService roleService) {
        this.authenticationProvider = provider;
        this.roleService = roleService;
    }

    @ApiOperation(value = "Check role USER for authorized user", response = ResponseEntity.class, tags = "status")
    @GetMapping("/check/user-status")
    public ResponseEntity<String> userStatus() {
        Authentication user = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(user);
        return user.getAuthorities().contains(roleService.getRoleByName("USER").orElseThrow())
                ? ResponseEntity.status(HttpStatus.OK).body("Authorization successful")
                : ResponseEntity.status(HttpStatus.FORBIDDEN).body("Authorization failed");
    }

    @ApiOperation(value = "Generate JWT token and authenticate user",
            response = ResponseEntity.class, tags = "token")
    @PostMapping("/auth/token")
    public ResponseEntity<String> token(@RequestBody AuthenticationRequest request) {
        String email = Objects.requireNonNull(request.getEmail());
        String password = Objects.requireNonNull(request.getPassword());
        log.info("Email is " + email);
        try {
            SecurityContextHolder.getContext().setAuthentication(authenticationProvider
                    .authenticate(new UsernamePasswordAuthenticationToken(email, password)));
        } catch (BadCredentialsException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.ok().body(
                JWT.create().withJWTId(email).withSubject(password)
                        .sign(Algorithm.HMAC256("PrinceNanadaime".getBytes())));
    }

    @GetMapping("/user/authentication")
    public ResponseEntity<?> getRegisterUser() {
        User registerUser = (User) SecurityContextHolder.getContext().getAuthentication().getDetails();
        if (registerUser.getAuthorities() == null) {
            return new ResponseEntity<>("Вам необходимо авторизоваться", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(registerUser, HttpStatus.OK);
    }
}