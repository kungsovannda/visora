package kh.edu.istad.visora.visora.infrastructure.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app.jwt")
public class JwtProperties {

    String secret;

    long accessTokenExpiration = 900;

    long refreshTokenExpiration = 604800;

}