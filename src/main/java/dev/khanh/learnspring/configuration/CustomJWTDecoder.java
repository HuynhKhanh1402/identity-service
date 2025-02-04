package dev.khanh.learnspring.configuration;

import com.nimbusds.jose.JOSEException;
import dev.khanh.learnspring.dto.request.IntrospectRequest;
import dev.khanh.learnspring.dto.respone.IntrospectResponse;
import dev.khanh.learnspring.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;

@Component
@RequiredArgsConstructor
public class CustomJWTDecoder implements JwtDecoder {
    @Value("${jwt.signerKey}")
    private String signerKey;

    private final AuthenticationService authenticationService;

    private NimbusJwtDecoder nimbusJwtDecoder = null;


    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            IntrospectResponse introspect = authenticationService.introspect(
                    IntrospectRequest
                            .builder()
                            .token(token)
                            .build()
            );
            if (!introspect.isValid()) {
                throw new JwtException("Token is invalid");
            }
        } catch (JOSEException | ParseException e) {
            throw new RuntimeException(e);
        }

        if (nimbusJwtDecoder == null) {
            SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HS256");
            nimbusJwtDecoder = NimbusJwtDecoder
                    .withSecretKey(secretKeySpec)
                    .macAlgorithm(MacAlgorithm.HS512)
                    .build();
        }

        return nimbusJwtDecoder.decode(token);
    }
}
