package com.itm.metube.controller;

import com.itm.metube.dto.LoginRequest;
import com.itm.metube.dto.RegisterRequest;
import com.itm.metube.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@ModelAttribute RegisterRequest registerRequest)
    {   System.out.println(registerRequest);
        if(authService.registerUser(registerRequest))
        {
            //if user registration is successful
            return new ResponseEntity<>(HttpStatus.OK);

        }
        else
        {
            return new ResponseEntity<>("User Exists with same email", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest loginRequest) throws Exception {
        System.out.println(loginRequest);
        return authService.login(loginRequest);
    }

    @PostMapping("/logout")
    public boolean logout() throws Exception {
        return authService.logout();
    }
}
