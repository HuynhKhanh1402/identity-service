package dev.khanh.learnspring.controller;

import com.nimbusds.jose.JOSEException;
import dev.khanh.learnspring.dto.request.ApiResponse;
import dev.khanh.learnspring.dto.request.AuthenticationRequest;
import dev.khanh.learnspring.dto.request.IntrospectRequest;
import dev.khanh.learnspring.dto.request.LogoutRequest;
import dev.khanh.learnspring.dto.respone.IntrospectResponse;
import dev.khanh.learnspring.dto.respone.AuthenticationResponse;
import dev.khanh.learnspring.service.AuthenticationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("/token")
    public ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        var result = authenticationService.authenticate(request);
        return ApiResponse
                .<AuthenticationResponse>builder()
                .result(result)
                .build();
    }

    @PostMapping("/introspect")
    public ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        var result = authenticationService.introspect(request);
        return ApiResponse
                .<IntrospectResponse>builder()
                .result(result)
                .build();
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(@RequestBody LogoutRequest logoutRequest) throws ParseException, JOSEException {
        authenticationService.logout(logoutRequest);
        return ApiResponse.<Void>builder().build();
    }
}
