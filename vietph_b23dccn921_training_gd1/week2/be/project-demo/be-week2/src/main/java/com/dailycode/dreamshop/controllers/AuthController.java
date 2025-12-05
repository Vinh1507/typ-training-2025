package com.dailycode.dreamshop.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dailycode.dreamshop.dto.DataResponse;
import com.dailycode.dreamshop.dto.LoginRequest;
import com.dailycode.dreamshop.dto.RegisterRequest;
import com.dailycode.dreamshop.service.UserService;
import com.dailycode.dreamshop.utils.jwt.JwtProvider;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtProvider jwtProvider;

    @PostMapping("/register")
    public ResponseEntity<DataResponse> register(@RequestBody @Valid RegisterRequest request){
        DataResponse res = userService.craete(request);
        return ResponseEntity.status(res.getStatus()).body(res);
    }
    @PostMapping("/login")
    public ResponseEntity<DataResponse>login(@RequestBody @Valid LoginRequest request){
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String access_token= jwtProvider.generateAccessToken(userDetails);
            String refresh_token= jwtProvider.generateRefreshToken(userDetails);
            ResponseCookie cookie = ResponseCookie.from("refresh_token",refresh_token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(7 * 24 * 60 * 60)
                .sameSite("Strict")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new DataResponse(HttpStatus.OK, true, null, access_token));

        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
            return ResponseEntity.badRequest().body(new DataResponse(HttpStatus.BAD_REQUEST, false, "Email or password is incorrect!",  null));
        }
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/me")
    public ResponseEntity<DataResponse> getProfile(@AuthenticationPrincipal UserDetails userDetails){
       DataResponse res = userService.getProfile(userDetails.getUsername());
       return ResponseEntity.status(res.getStatus()).body(res);
    }
   @GetMapping("/refresh-token")
public ResponseEntity<DataResponse> refreshToken(HttpServletRequest request) {
    try {
        String newAccessToken = jwtProvider.refreshToken(request);
        if (newAccessToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new DataResponse(HttpStatus.UNAUTHORIZED, false, "Invalid or expired refresh token", null));
        }

        return ResponseEntity.ok()
                .body(new DataResponse(HttpStatus.OK, true, null, newAccessToken));

    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new DataResponse(HttpStatus.UNAUTHORIZED, false, "Could not refresh token", null));
    }
}
}
