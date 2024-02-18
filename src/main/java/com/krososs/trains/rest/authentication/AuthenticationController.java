package com.krososs.trains.rest.authentication;

import com.krososs.trains.rest.authentication.DTO.RegisterRequest;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import com.krososs.trains.rest.authentication.DTO.LoginRequest;
import com.krososs.trains.rest.util.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequest registerRequest, BindingResult result){

        if (result.hasErrors())
            return new ResponseEntity<>(Response.getErrorResponse(result.getAllErrors()), HttpStatus.BAD_REQUEST);
        if(authenticationService.usernameExists(registerRequest.getUsername()))
            return new ResponseEntity<>("Username is already taken.", HttpStatus.BAD_REQUEST);
        if(authenticationService.emailExists(registerRequest.getEmail()))
            return new ResponseEntity<>("Email is already in use.", HttpStatus.BAD_REQUEST);
        authenticationService.register(registerRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
        if(!authenticationService.usernameExists(loginRequest.getUsername()))
            return new ResponseEntity<>("Invalid username.", HttpStatus.BAD_REQUEST);
        if(!authenticationService.correctPassword(loginRequest))
            return new ResponseEntity<>("Invalid password.", HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(authenticationService.login(loginRequest),HttpStatus.OK);
    }
}