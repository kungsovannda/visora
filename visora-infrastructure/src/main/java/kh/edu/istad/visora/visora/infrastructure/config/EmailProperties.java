package kh.edu.istad.visora.visora.infrastructure.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "spring.mail")
public class EmailProperties {

    String displayName;
    String logo;
    String projectUrl;

}
