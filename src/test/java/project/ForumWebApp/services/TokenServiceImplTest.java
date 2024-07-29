package project.ForumWebApp.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import project.ForumWebApp.services.Implementations.TokenServiceImpl;

@ExtendWith(MockitoExtension.class)
class TokenServiceImplTest {

    @Mock
    private JwtEncoder jwtEncoder;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private TokenServiceImpl tokenService;

    private Instant now;
    private Instant expiry;

    @BeforeEach
    void setUp() {
        now = Instant.now();
        expiry = now.plus(1, ChronoUnit.DAYS);
    }

    @Test
    void generateJwt() {

        when(authentication.getName()).thenReturn("testuser");

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(expiry)
                .subject("testuser")
                .claim("roles", "ROLE_USER")
                .build();

        Jwt jwt = mock(Jwt.class);
        when(jwt.getTokenValue()).thenReturn("token");

        when(jwtEncoder.encode(any(JwtEncoderParameters.class))).thenReturn(jwt);


        String token = tokenService.generateJwt(authentication);


        assertEquals("token", token);
    }
}
