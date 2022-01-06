package com.itm.metube.service;

import com.itm.metube.dto.LoginRequest;
import com.itm.metube.dto.RegisterRequest;
import com.itm.metube.model.User;
import com.itm.metube.repository.UserRepository;
import com.itm.metube.security.JwtProvider;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthService {
    private final UserRepository userRepositroy;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final S3Service s3Service;

    public boolean registerUser(RegisterRequest registerRequest)
    {
        Optional<User> optionaluser=userRepositroy.findByEmail(registerRequest.getEmail());

        if(optionaluser.isPresent())
        {
            //if user exists
            System.out.println("User"+optionaluser);
            return false;
        }

        //if no user exists
        User user=new User();
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setCreated(Instant.now());

        //upload profile photo to s3
        user.setProfilePhotoUrl(s3Service.uploadFile(registerRequest.getProfilePhoto()));

        userRepositroy.save(user);

        return true;

    }

    public ResponseEntity<Map<String, Object>> login(LoginRequest loginRequest) throws Exception
    {
        Authentication authenticate=authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token=jwtProvider.generateToken(authenticate);
        Map<String, Object> response = new HashMap<>();
        response.put("token",token);

        User user=getCurrentUser();


        response.put("name",user.getFirstName()+" "+user.getLastName());
        response.put("profilePhotoUrl",user.getProfilePhotoUrl());
        response.put("userId",user.getId());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    public User getCurrentUser() {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        return userRepositroy.findByEmail(principal.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Email name not found - " + principal.getUsername()));
    }

    public boolean logout() {
        SecurityContextHolder.getContext().setAuthentication(null);
        return true;
    }
}
